package com.yoloo.server.user.domain.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.common.cache.CacheService
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.common.util.ServiceExceptions.checkConflict
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import com.yoloo.server.user.infrastructure.event.RefreshFeedEvent
import com.yoloo.server.user.infrastructure.event.UserRelationshipEvent
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProviderFactory
import com.yoloo.server.user.infrastructure.util.groupinfo.GroupInfoFetcher
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
    private val cacheService: CacheService,
    private val eventPublisher: ApplicationEventPublisher
) : UseCase<InsertUserUseCase.Request, UserResponse> {

    override fun execute(request: Request): UserResponse {
        val payload = request.payload

        val filter = cacheService.get("filter-user-exists") as NanoCuckooFilter?

        checkUserExists(filter, payload.email!!, payload.username!!)

        val providerType = ProviderType.valueOf(payload.providerType!!.toUpperCase())
        val userInfoProvider = userInfoProviderFactory.create(providerType)
        val userInfo = userInfoProvider.getUserInfo(payload.providerIdToken)
        val subscribedGroupIds = payload.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUserFcmTokens = getFollowedUserFcmTokens(payload.followedUserIds.orEmpty())

        val user = User(
            id = idGenerator.generateId(),
            displayName = UserDisplayName(payload.displayName!!),
            username = Username(payload.username),
            email = Email(payload.email),
            provider = SocialProvider(userInfo.providerId, userInfo.providerType),
            image = AvatarImage(userInfo.picture),
            gender = Gender.valueOf(payload.gender!!.toUpperCase()),
            fcmToken = payload.fcmToken!!,
            scopes = setOf("user:read", "user:write", "post:read", "post:write"),
            lastKnownIP = IP(payload.lastKnownIP!!),
            locale = UserLocale(Locale.ENGLISH.language, "en_US"),
            subscribedGroups = groups,
            self = true
        )

        ofy().transact {
            ofy().save().entity(user)

            populateUserExistsFilter(filter, user.username.value, user.email.value)

            publishGroupSubscriptionEvent(user)
            publishUserRelationshipEvent(followedUserFcmTokens)
            publishRefreshFeedEvent(subscribedGroupIds)
        }

        return userResponseMapper.apply(user)
    }

    private fun populateUserExistsFilter(filter: NanoCuckooFilter?, username: String, email: String) {
        val newFilter = filter ?: NanoCuckooFilter.Builder(32).build()
        newFilter.insert(username)
        newFilter.insert(email)
        cacheService.putAsync("filter-user-exists", newFilter)
    }

    private fun publishRefreshFeedEvent(subscribedGroupIds: List<Long>) {
        eventPublisher.publishEvent(RefreshFeedEvent(this, subscribedGroupIds))
    }

    private fun publishUserRelationshipEvent(followedUserFcmTokens: List<String>) {
        if (followedUserFcmTokens.isNotEmpty()) {
            eventPublisher.publishEvent(UserRelationshipEvent(this, followedUserFcmTokens))
        }
    }

    private fun publishGroupSubscriptionEvent(user: User) {
        val userInfo = GroupSubscriptionEvent.UserInfo(user.id, user.displayName, user.image)
        val groupIds = user.subscribedGroups.map { it.id }
        eventPublisher.publishEvent(GroupSubscriptionEvent(this, userInfo, groupIds))
    }

    private fun getFollowedUserFcmTokens(userIds: List<Long>): List<String> {
        return when {
            userIds.isEmpty() -> emptyList()
            else -> ofy().load().type(User::class.java).ids(userIds).values.map { it.fcmToken }
        }
    }

    private fun checkUserExists(filter: NanoCuckooFilter?, email: String, username: String) {
        val exists = when (filter) {
            null -> getUserKeyByEmail(email) != null || getUserKeyByUsername(username) != null
            else -> filter.contains(email) || filter.contains(username)
        }

        checkConflict(!exists, "user.register.exists")
    }

    private fun getUserKeyByEmail(email: String): Key<User>? {
        return ofy().load()
            .type(User::class.java)
            .filter("email.value", email)
            .keys()
            .first()
            .now()
    }

    private fun getUserKeyByUsername(email: String): Key<User>? {
        return ofy().load()
            .type(User::class.java)
            .filter("username.value", email)
            .keys()
            .first()
            .now()
    }

    class Request(val payload: InsertUserPayload)
}