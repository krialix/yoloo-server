package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkConflict
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.entity.UserFilter
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import com.yoloo.server.user.infrastructure.event.RefreshFeedEvent
import com.yoloo.server.user.infrastructure.event.RelationshipEvent
import com.yoloo.server.user.infrastructure.fetcher.groupinfo.GroupInfoFetcher
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProviderFactory
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
    private val eventPublisher: ApplicationEventPublisher
) : UseCase<InsertUserUseCase.Request, UserResponse> {

    override fun execute(request: Request): UserResponse {
        val payload = request.payload

        val userFilter = getUserFilter()

        checkConflict(!userFilter.filter.contains(payload.email), "user.register.exists")

        val providerType = ProviderType.valueOf(payload.providerType!!.toUpperCase())
        val userInfoProvider = userInfoProviderFactory.create(providerType)
        val userInfo = userInfoProvider.getUserInfo(payload.providerIdToken)
        val subscribedGroupIds = payload.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUserIdFcmTokenMap = getFollowedUserIdFcmTokenMap(payload.followedUserIds.orEmpty())
        val user = createUser(payload, userInfo, groups)

        saveTx(user, userFilter, followedUserIdFcmTokenMap, subscribedGroupIds)

        return userResponseMapper.apply(user)
    }

    private fun saveTx(
        user: User,
        userFilter: UserFilter,
        followedUserIdFcmTokenMap: Map<Long, String>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            ofy().defer().save().entity(user)

            addUserToExistsFilter(userFilter, user.account.email.value, user.id)

            publishGroupSubscriptionEvent(user)
            publishUserRelationshipEvent(user, followedUserIdFcmTokenMap)
            publishRefreshFeedEvent(subscribedGroupIds)
        }
    }

    private fun getUserFilter(): UserFilter {
        return ofy().load().type(UserFilter::class.java).id(Filters.KEY_FILTER_USERS).now() ?: UserFilter()
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

    private fun addUserToExistsFilter(userFilter: UserFilter, email: String, userId: Long) {
        val filter = userFilter.filter
        filter.insert(email)
        filter.insert(userId)

        ofy().defer().save().entity(userFilter)
    }

    private fun publishRefreshFeedEvent(subscribedGroupIds: List<Long>) {
        eventPublisher.publishEvent(RefreshFeedEvent(this, subscribedGroupIds))
    }

    private fun publishUserRelationshipEvent(user: User, map: Map<Long, String>) {
        if (map.isNotEmpty()) {
            val event = RelationshipEvent(
                this,
                idGenerator.generateId(),
                user.id,
                user.profile.displayName,
                user.profile.image,
                map
            )

            eventPublisher.publishEvent(event)
        }
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

    private fun getFollowedUserIdFcmTokenMap(userIds: List<Long>): Map<Long, String> {
        return when {
            userIds.isEmpty() -> emptyMap()
            else -> ofy().load().type(User::class.java).ids(userIds).values
                .associateBy({ it.id }, { it.account.fcmToken })
        }
    }

    class Request(val payload: InsertUserPayload)
}