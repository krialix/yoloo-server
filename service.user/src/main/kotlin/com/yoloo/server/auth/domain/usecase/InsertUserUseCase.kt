package com.yoloo.server.auth.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.domain.entity.OauthUser
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkConflict
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.infrastructure.event.RelationshipEvent
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
import org.springframework.security.crypto.password.PasswordEncoder
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
    private val memcacheService: MemcacheService,
    private val passwordEncoder: PasswordEncoder
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
        val account = createAccount(user.id, payload, userInfo)

        saveTx(user, account, emailFilter, followedUsers, subscribedGroupIds)

        return userResponseMapper.apply(user)
    }

    private fun saveTx(
        user: User,
        oauthUser: OauthUser,
        emailFilter: NanoCuckooFilter,
        followedUsers: Collection<User>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            ofy().defer().save().entities(user, oauthUser)

            addEmailToEmailFilter(emailFilter, oauthUser.email.value)

            publishGroupSubscriptionEvent(user)
            followedUsers.forEach { publishFollowEvent(user, it) }
            publishRefreshFeedEvent(subscribedGroupIds)
        }
    }

    private fun createUser(payload: InsertUserPayload, userInfo: UserInfo, groups: List<UserGroup>): User {
        return User(
            id = idGenerator.generateId(),
            profile = Profile(
                displayName = DisplayName(payload.displayName!!),
                image = AvatarImage(Url(userInfo.picture)),
                gender = Gender.valueOf(payload.gender!!.toUpperCase()),
                locale = UserLocale(Locale.ENGLISH.language, "en_US")
            ),
            subscribedGroups = groups,
            fcmToken = payload.fcmToken!!,
            email = Email(payload.email!!),
            self = true
        )
    }

    private fun createAccount(userId: Long, payload: InsertUserPayload, userInfo: UserInfo): OauthUser {
        return OauthUser(
            id = "oauth:$userId",
            email = Email(payload.email!!),
            provider = SocialProvider(userInfo.providerId, userInfo.providerType),
            scopes = setOf("user:read", "user:write", "post:read", "post:write"),
            lastKnownIP = IP(payload.lastKnownIP!!),
            password = payload.password?.let { Password(passwordEncoder.encode(it)) },
            displayName = DisplayName(payload.displayName!!),
            image = AvatarImage(Url(userInfo.picture))
        )
    }

    private fun addEmailToEmailFilter(emailFilter: NanoCuckooFilter, email: String) {
        emailFilter.insert(email)
        memcacheService.put(Filters.KEY_FILTER_EMAIL, emailFilter)
    }

    private fun publishRefreshFeedEvent(subscribedGroupIds: List<Long>) {
        eventPublisher.publishEvent(RefreshFeedEvent(this, subscribedGroupIds))
    }

    private fun publishFollowEvent(fromUser: User, toUser: User) {
        val event = RelationshipEvent.Follow(
            this,
            fromUser.id,
            fromUser.profile.displayName,
            fromUser.profile.image,
            toUser.id,
            toUser.fcmToken,
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
        return memcacheService.get(Filters.KEY_FILTER_EMAIL) as NanoCuckooFilter
    }

    class Request(val payload: InsertUserPayload)
}