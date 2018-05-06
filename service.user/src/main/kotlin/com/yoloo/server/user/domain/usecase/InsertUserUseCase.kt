package com.yoloo.server.user.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkConflict
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.infrastructure.event.FollowEvent
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import com.yoloo.server.user.infrastructure.event.RefreshFeedEvent
import com.yoloo.server.user.infrastructure.fetcher.groupinfo.GroupInfoFetcher
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProviderFactory
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertUserUseCase(
    private val userResponseMapper: UserResponseMapper,
    private val userInfoProviderFactory: UserInfoProviderFactory,
    private val groupInfoFetcher: GroupInfoFetcher,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
    private val memcacheService: MemcacheService
) : UseCase<InsertUserUseCase.Request, UserResponse> {

    override fun execute(request: Request): UserResponse {
        val payload = request.payload

        val emailFilter = getEmailFilter()

        checkConflict(!emailFilter.contains(payload.email), "user.error.exists")

        val providerType = ProviderType.valueOf(payload.providerType!!.toUpperCase())
        val userInfoProvider = userInfoProviderFactory.create(providerType)
        val userInfo = userInfoProvider.getUserInfo(payload.providerIdToken)
        val subscribedGroupIds = payload.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUsers = getFollowedUsers(payload.followedUserIds.orEmpty())
        val user = createUser(payload, userInfo, groups)

        saveTx(user, emailFilter, followedUsers, subscribedGroupIds)

        return userResponseMapper.apply(user)
    }

    private fun saveTx(
        user: User,
        emailFilter: NanoCuckooFilter,
        followedUsers: Collection<User>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            ofy().defer().save().entity(user)

            addUserToExistsFilter(emailFilter, user.account.email.value)

            publishGroupSubscriptionEvent(user)
            followedUsers.forEach { publishFollowEvent(user, it) }
            publishRefreshFeedEvent(subscribedGroupIds)
        }
    }

    private fun createUser(payload: InsertUserPayload, userInfo: UserInfo, groups: List<UserGroup>): User {
        return User(
            id = idGenerator.generateId(),
            profile = createProfile(payload, userInfo),
            account = createAccount(payload, userInfo),
            subscribedGroups = groups,
            self = true
        )
    }

    private fun createProfile(payload: InsertUserPayload, userInfo: UserInfo): Profile {
        return Profile(
            displayName = DisplayName(payload.displayName!!),
            image = AvatarImage(Url(userInfo.picture)),
            gender = Gender.valueOf(payload.gender!!.toUpperCase()),
            locale = UserLocale(Locale.ENGLISH.language, "en_US")
        )
    }

    private fun createAccount(payload: InsertUserPayload, userInfo: UserInfo): Account {
        return Account(
            email = Email(payload.email!!),
            provider = SocialProvider(userInfo.providerId, userInfo.providerType),
            fcmToken = payload.fcmToken!!,
            scopes = setOf("user:read", "user:write", "post:read", "post:write"),
            lastKnownIP = IP(payload.lastKnownIP!!)
        )
    }

    private fun addUserToExistsFilter(emailFilter: NanoCuckooFilter, email: String) {
        emailFilter.insert(email);
        memcacheService.put(Filters.KEY_FILTER_EMAIL, emailFilter)
    }

    private fun publishRefreshFeedEvent(subscribedGroupIds: List<Long>) {
        eventPublisher.publishEvent(RefreshFeedEvent(this, subscribedGroupIds))
    }

    private fun publishFollowEvent(fromUser: User, toUser: User) {
        val event = FollowEvent(
            this,
            fromUser.id,
            fromUser.profile.displayName,
            fromUser.profile.image,
            toUser.id,
            toUser.account.fcmToken,
            objectMapper
        )

        eventPublisher.publishEvent(event)
    }

    private fun publishGroupSubscriptionEvent(user: User) {
        val event = GroupSubscriptionEvent(
            this,
            idGenerator.generateId(),
            user.id,
            user.profile.displayName,
            user.profile.image,
            user.subscribedGroups.map { it.id }
        )

        eventPublisher.publishEvent(event)
    }

    private fun getFollowedUsers(userIds: List<Long>): Collection<User> {
        return when {
            userIds.isEmpty() -> emptyList()
            else -> ofy().load().type(User::class.java).ids(userIds).values
        }
    }

    private fun getEmailFilter(): NanoCuckooFilter {
        return memcacheService.get(Filters.KEY_FILTER_EMAIL) as NanoCuckooFilter? ?: ofy()
            .load()
            .type(User::class.java)
            //.project("account.email.value")
            .list()
            .asSequence()
            .map { it.account.email.value }
            .toList()
            .let {
                println("Creating new cuckoo filter for email")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it) }

                memcacheService.put(Filters.KEY_FILTER_EMAIL, cuckooFilter)
                return@let cuckooFilter
            }
    }

    class Request(val payload: InsertUserPayload)
}