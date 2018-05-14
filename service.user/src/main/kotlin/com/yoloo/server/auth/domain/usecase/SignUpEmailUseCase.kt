package com.yoloo.server.auth.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.domain.entity.Account
import com.yoloo.server.auth.domain.request.SignUpEmailRequest
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.infrastructure.event.RelationshipEvent
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import com.yoloo.server.user.infrastructure.event.RefreshFeedEvent
import com.yoloo.server.user.infrastructure.fetcher.groupinfo.GroupInfoFetcher
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProviderFactory
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.stereotype.Component
import java.util.*

@Component
class SignUpEmailUseCase(
    private val userInfoProviderFactory: UserInfoProviderFactory,
    private val groupInfoFetcher: GroupInfoFetcher,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
    private val memcacheService: MemcacheService
) : UseCase<SignUpEmailUseCase.Params, OAuth2AccessToken> {

    override fun execute(params: Params): OAuth2AccessToken {
        val payload = params.payload

        val emailFilter = getEmailFilter()

        ServiceExceptions.checkConflict(!emailFilter.contains(payload.email), "user.error.exists")

        val userInfoProvider = userInfoProviderFactory.create(ProviderType.EMAIL)
        val userInfo = userInfoProvider.getUserInfo(null)
        val subscribedGroupIds = payload.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUsers = getFollowedUsers(payload.followedUserIds.orEmpty())
        val user = createUser(payload, userInfo, groups)
        val account = createAccount(user.id, payload, userInfo)

        saveTx(user, account, emailFilter, followedUsers, subscribedGroupIds)

        val passwordResourceDetails =
            getResourceOwnerPasswordResourceDetails(account.email.value, account.password!!.value)

        val accessTokenProvider = ResourceOwnerPasswordAccessTokenProvider()
        return accessTokenProvider.obtainAccessToken(passwordResourceDetails, DefaultAccessTokenRequest())
    }

    private fun getResourceOwnerPasswordResourceDetails(
        email: String,
        password: String
    ): ResourceOwnerPasswordResourceDetails {
        val passwordResourceDetails = ResourceOwnerPasswordResourceDetails()
        passwordResourceDetails.username = email
        passwordResourceDetails.password = password
        passwordResourceDetails.clientId = "trusted_mobile"
        passwordResourceDetails.clientSecret = "secret"
        passwordResourceDetails.accessTokenUri = "http://localhost:8081/oauth/token"
        return passwordResourceDetails
    }

    private fun saveTx(
        user: User,
        account: Account,
        emailFilter: NanoCuckooFilter,
        followedUsers: Collection<User>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            ofy().save().entities(user, account).now()

            addEmailToEmailFilter(emailFilter, account.email.value)

            publishGroupSubscriptionEvent(user)
            followedUsers.forEach { publishFollowEvent(user, it) }
            publishRefreshFeedEvent(subscribedGroupIds)
        }
    }

    private fun createUser(payload: SignUpEmailRequest, userInfo: UserInfo, groups: List<UserGroup>): User {
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

    private fun createAccount(userId: Long, payload: SignUpEmailRequest, userInfo: UserInfo): Account {
        return Account(
            id = "oauth:$userId",
            email = Email(payload.email!!),
            provider = SocialProvider(userInfo.providerId, userInfo.providerType),
            scopes = setOf("user:read", "user:write", "post:read", "post:write"),
            lastKnownIP = IP(payload.device!!.localIp!!),
            password = payload.password?.let { Password(it) },
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

    class Params(val payload: SignUpEmailRequest)
}