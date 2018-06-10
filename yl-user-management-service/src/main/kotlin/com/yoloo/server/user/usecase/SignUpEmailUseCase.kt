package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.IP
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.exception.ServiceExceptions
import com.yoloo.server.user.entity.User
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

class SignUpEmailUseCase(
    private val groupInfoFetcher: GroupInfoFetcher,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
    private val memcacheService: MemcacheService,
    private val resourceOwnerPasswordResourceDetails: ResourceOwnerPasswordResourceDetails,
    private val accessTokenProvider: AccessTokenProvider
) {

    fun execute(request: SignUpEmailRequest): OAuth2AccessToken {
        val emailFilter = getEmailFilter()

        val email = request.email!!
        ServiceExceptions.checkConflict(!emailFilter.contains(email), "user.error.exists")

        val metaData = request.metadata!!

        val subscribedGroupIds = metaData.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUsers = getFollowedUsers(metaData.followedUserIds.orEmpty())

        val user = createUser(
            clientId = request.clientId!!,
            email = email,
            password = request.password!!,
            metadata = metaData,
            groups = groups
        )

        addEmailToEmailFilter(emailFilter, email)

        saveTx(user, followedUsers, subscribedGroupIds)

        return getAccessToken(user)
    }

    private fun getAccessToken(user: User): OAuth2AccessToken {
        val passwordResourceDetails =
            getResourceOwnerPasswordResourceDetails(user.email.value, user.password!!.value)

        return accessTokenProvider.obtainAccessToken(
            passwordResourceDetails,
            DefaultAccessTokenRequest()
        )
    }

    private fun getResourceOwnerPasswordResourceDetails(
        email: String,
        password: String
    ): ResourceOwnerPasswordResourceDetails {
        resourceOwnerPasswordResourceDetails.username = email
        resourceOwnerPasswordResourceDetails.password = password
        return resourceOwnerPasswordResourceDetails
    }

    private fun saveTx(user: User, followedUsers: Collection<User>, subscribedGroupIds: List<Long>) {
        ofy().transact {
            ofy().save().entity(user).now()

            publishGroupSubscriptionEvent(user)
            followedUsers.forEach { publishFollowEvent(user, it) }
            publishRefreshFeedEvent(subscribedGroupIds)
        }
    }

    private fun createUser(
        clientId: String,
        email: String,
        password: String,
        metadata: SignUpEmailRequest.UserMetadata,
        groups: List<UserGroup>
    ): User {
        val app = metadata.app!!
        val device = metadata.device!!
        val screen = device.screen!!
        val os = device.os!!

        return User(
            id = idGenerator.generateId(),
            email = Email(email),
            provider = Provider(null, Provider.Type.EMAIL),
            authorities = setOf(User.Authority.MEMBER),
            localIp = IP(metadata.device!!.localIp!!),
            password = Password(password),
            fcmToken = metadata.fcmToken!!,
            clientId = clientId,
            profile = Profile(
                displayName = DisplayName(metadata.displayName!!),
                image = AvatarImage(Url(DEFAULT_IMAGE_URL)),
                gender = Gender.valueOf(metadata.gender!!.toUpperCase()),
                locale = UserLocale(metadata.language!!, metadata.country!!)
            ),
            subscribedGroups = groups,
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
        memcacheService.put(User.KEY_FILTER_EMAIL, emailFilter)
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
        return memcacheService.get(User.KEY_FILTER_EMAIL) as NanoCuckooFilter
    }

    companion object {
        const val DEFAULT_IMAGE_URL = ""
    }
}