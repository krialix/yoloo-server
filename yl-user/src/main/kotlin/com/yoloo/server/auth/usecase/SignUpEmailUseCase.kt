package com.yoloo.server.auth.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.entity.Account
import com.yoloo.server.auth.vo.Provider
import com.yoloo.server.auth.vo.SignUpEmailRequest
import com.yoloo.server.auth.vo.UserLocale
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.entity.UserMeta
import com.yoloo.server.user.event.GroupSubscriptionEvent
import com.yoloo.server.user.event.RefreshFeedEvent
import com.yoloo.server.user.event.RelationshipEvent
import com.yoloo.server.user.fetcher.GroupInfoFetcher
import com.yoloo.server.user.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.oauth2.client.token.AccessTokenProvider
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.stereotype.Component

@Component
internal class SignUpEmailUseCase(
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

        addEmailToEmailFilter(emailFilter, account.email.value)

        saveTx(user, account, userMeta, followedUsers, subscribedGroupIds)

        return getAccessToken(account)
    }

    private fun getAccessToken(account: Account): OAuth2AccessToken {
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
        followedUsers: Collection<Account>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            ofy().save().entities(user, account, userMeta).now()

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
        val app = payload.app!!
        val device = payload.device!!
        val screen = device.screen!!
        val os = device.os!!

        return UserMeta(
            id = userId,
            appInfo = AppInfo(
                firstInstallTime = app.firstInstallTime!!,
                lastUpdateTime = app.lastUpdateTime!!,
                googleAdsId = app.googleAdvertisingId!!
            ),
            device = Device(
                brand = device.brand!!,
                model = device.model!!,
                screen = Screen(
                    dpi = Screen.Dpi(screen.dpi!!),
                    height = screen.height!!,
                    width = screen.width!!
                ),
                localIp = IP(device.localIp!!),
                os = Os(
                    type = Os.Type.valueOf(os.type!!.toUpperCase()),
                    version = Os.Version(os.version!!)
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