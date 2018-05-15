package com.yoloo.server.auth.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.domain.entity.Account
import com.yoloo.server.auth.domain.request.SignUpEmailRequest
import com.yoloo.server.auth.domain.vo.Provider
import com.yoloo.server.auth.domain.vo.UserLocale
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.infrastructure.event.RelationshipEvent
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.entity.UserMeta
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import com.yoloo.server.user.infrastructure.event.RefreshFeedEvent
import com.yoloo.server.user.infrastructure.fetcher.groupinfo.GroupInfoFetcher
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.oauth2.client.token.AccessTokenProvider
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.stereotype.Component

@Component
class SignUpEmailUseCase(
    private val groupInfoFetcher: GroupInfoFetcher,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
    private val memcacheService: MemcacheService,
    private val resourceOwnerPasswordResourceDetails: ResourceOwnerPasswordResourceDetails,
    private val accessTokenProvider: AccessTokenProvider
) : UseCase<SignUpEmailUseCase.Params, OAuth2AccessToken> {

    override fun execute(params: Params): OAuth2AccessToken {
        val payload = params.payload

        val emailFilter = getEmailFilter()

        ServiceExceptions.checkConflict(!emailFilter.contains(payload.email), "user.error.exists")

        val subscribedGroupIds = payload.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUsers = getFollowedUsers(payload.followedUserIds.orEmpty())

        val user = createUser(payload, groups)
        val account = createAccount(user.id, payload)
        val userMeta = createUserMeta(user.id, payload)

        saveTx(user, account, userMeta, emailFilter, followedUsers, subscribedGroupIds)

        val passwordResourceDetails =
            getResourceOwnerPasswordResourceDetails(account.email.value, account.password!!.value)

        return accessTokenProvider.obtainAccessToken(passwordResourceDetails, DefaultAccessTokenRequest())
    }

    private fun getResourceOwnerPasswordResourceDetails(
        email: String,
        password: String
    ): ResourceOwnerPasswordResourceDetails {
        resourceOwnerPasswordResourceDetails.username = email
        resourceOwnerPasswordResourceDetails.password = password
        return resourceOwnerPasswordResourceDetails
    }

    private fun saveTx(
        user: User,
        account: Account,
        userMeta: UserMeta,
        emailFilter: NanoCuckooFilter,
        followedUsers: Collection<Account>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            ofy().save().entities(user, account, userMeta).now()

            addEmailToEmailFilter(emailFilter, account.email.value)

            publishGroupSubscriptionEvent(user)
            followedUsers.forEach { publishFollowEvent(account, it) }
            publishRefreshFeedEvent(subscribedGroupIds)
        }
    }

    private fun createUser(payload: SignUpEmailRequest, groups: List<UserGroup>): User {
        return User(
            id = idGenerator.generateId(),
            email = Email(payload.email!!),
            profile = Profile(
                displayName = DisplayName(payload.displayName!!),
                image = AvatarImage(Url(DEFAULT_IMAGE_URL)),
                gender = Gender.valueOf(payload.gender!!.toUpperCase()),
                locale = UserLocale(payload.language!!, payload.country!!)
            ),
            subscribedGroups = groups,
            self = true
        )
    }

    private fun createAccount(userId: Long, payload: SignUpEmailRequest): Account {
        return Account(
            id = userId,
            email = Email(payload.email!!),
            provider = Provider(null, Provider.Type.EMAIL),
            authorities = setOf(Account.Authority.MEMBER),
            localIp = IP(payload.device!!.localIp!!),
            password = payload.password?.let { Password(it) },
            displayName = DisplayName(payload.displayName!!),
            image = AvatarImage(Url(DEFAULT_IMAGE_URL)),
            fcmToken = payload.fcmToken!!
        )
    }

    private fun createUserMeta(userId: Long, payload: SignUpEmailRequest): UserMeta {
        return UserMeta(
            id = userId,
            appInfo = AppInfo(
                firstInstallTime = payload.app!!.firstInstallTime!!,
                lastUpdateTime = payload.app.lastUpdateTime!!,
                googleAdsId = payload.app.googleAdvertisingId!!
            ),
            device = Device(
                brand = payload.device!!.brand!!,
                model = payload.device.model!!,
                screen = Screen(
                    dpi = Screen.Dpi(payload.device.screen!!.dpi!!),
                    height = payload.device.screen.height!!,
                    width = payload.device.screen.width!!
                ),
                localIp = IP(payload.device.localIp!!),
                os = Os(
                    type = Os.Type.valueOf(payload.device.os!!.type!!.toUpperCase()),
                    version = Os.Version(payload.device.os.version!!)
                )
            )
        )
    }

    private fun addEmailToEmailFilter(emailFilter: NanoCuckooFilter, email: String) {
        emailFilter.insert(email)
        memcacheService.put(Filters.KEY_FILTER_EMAIL, emailFilter)
    }

    private fun publishRefreshFeedEvent(subscribedGroupIds: List<Long>) {
        eventPublisher.publishEvent(RefreshFeedEvent(this, subscribedGroupIds))
    }

    private fun publishFollowEvent(fromUser: Account, toUser: Account) {
        val event = RelationshipEvent.Follow(
            this,
            fromUser.id,
            fromUser.displayName,
            fromUser.image,
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

    private fun getFollowedUsers(userIds: List<Long>): Collection<Account> {
        return when {
            userIds.isEmpty() -> emptyList()
            else -> ofy().load().type(Account::class.java).ids(userIds).values
        }
    }

    private fun getEmailFilter(): NanoCuckooFilter {
        return memcacheService.get(Filters.KEY_FILTER_EMAIL) as NanoCuckooFilter
    }

    class Params(val payload: SignUpEmailRequest)

    companion object {
        const val DEFAULT_IMAGE_URL = ""
    }
}