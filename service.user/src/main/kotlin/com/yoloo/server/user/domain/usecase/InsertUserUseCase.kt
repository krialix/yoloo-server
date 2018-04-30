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
import com.yoloo.server.user.infrastructure.event.UserRelationshipEvent
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.RequestPayload
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
        val providerType = ProviderType.valueOf(payload.providerType!!.toUpperCase())
        val userInfoProvider = userInfoProviderFactory.create(providerType)
        val requestPayload = createRequestPayload(payload)
        val userInfo = userInfoProvider.getUserInfo(requestPayload)

        checkUserEmailExists(userInfo.email)

        val groups = groupInfoFetcher.fetch(payload.subscribedGroupIds!!)

        val followedUserFcmTokens = getFollowedUserFcmTokens(payload)

        // todo increase group counts via user following group list
        // todo create user feed task and populate feed with group posts

        val user = User(
            id = idGenerator.generateId(),
            displayName = UserDisplayName(userInfo.displayName),
            email = Email(userInfo.email),
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

            eventPublisher.publishEvent(GroupSubscriptionEvent(this, payload.subscribedGroupIds!!))
            eventPublisher.publishEvent(UserRelationshipEvent(this, followedUserFcmTokens))
        }

        return userResponseMapper.apply(user)
    }

    private fun getFollowedUserFcmTokens(userPayload: InsertUserPayload): List<String> {
        return userPayload.followedUserIds
            ?.map { Key.create<User>(it) }
            .let { ofy().load().keys(it).values }
            .map { it.fcmToken }
    }

    private fun checkUserEmailExists(email: String) {
        val filter = cacheService.get("email-filter") as NanoCuckooFilter?

        val exists = when (filter) {
            null -> getUserKeyByEmail(email) != null
            else -> filter.contains(email)
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

    private fun createRequestPayload(payload: InsertUserPayload): RequestPayload {
        return RequestPayload(
            clientId = payload.clientId!!,
            subscribedGroupIds = payload.subscribedGroupIds!!,
            token = payload.token,
            displayName = payload.displayName,
            email = payload.email,
            password = payload.password
        )
    }

    class Request(val payload: InsertUserPayload)
}