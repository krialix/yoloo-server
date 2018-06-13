package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.google.firebase.auth.FirebaseAuth
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.IP
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.exception.ServiceExceptions
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.fetcher.GroupInfoFetcher
import com.yoloo.server.user.provider.EmailUserRecordProvider
import com.yoloo.server.user.provider.FacebookUserRecordProvider
import com.yoloo.server.user.provider.GoogleUserRecordProvider
import com.yoloo.server.user.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.password.PasswordEncoder
import java.lang.IllegalArgumentException

class UserRegisterUseCase(
    private val groupInfoFetcher: GroupInfoFetcher,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val objectMapper: ObjectMapper,
    private val firebaseAuth: FirebaseAuth,
    private val memcacheService: MemcacheService,
    private val passwordEncoder: PasswordEncoder
) {

    fun execute(request: UserRegisterRequest): String {
        val userIdentityFilter = getUserIdentityFilter()

        ServiceExceptions.checkConflict(!userIdentityFilter.contains(User.KEY_FILTER_USER_IDENTIFIER), "user.conflict")

        createFirebaseUser(request)

        val subscribedGroupIds = request.subscribedGroupIds!!
        val groups = groupInfoFetcher.fetch(subscribedGroupIds)
        val followedUsers = getFollowedUsers(request.followedUserIds.orEmpty())

        val user = createDbUser(request, groups)

        saveTx(user, userIdentityFilter, followedUsers, subscribedGroupIds)

        return createCustomToken(user.id)
    }

    private fun createFirebaseUser(request: UserRegisterRequest) {
        val userRecordProvider = when (request.providerId) {
            "google" -> GoogleUserRecordProvider(idGenerator)
            "facebook" -> FacebookUserRecordProvider(idGenerator)
            "email" -> EmailUserRecordProvider(idGenerator, passwordEncoder)
            else -> throw IllegalArgumentException("Given provider id is not valid")
        }

        val userRecord = userRecordProvider.provide(request)
        firebaseAuth.importUsers(listOf(userRecord))
    }

    private fun createDbUser(request: UserRegisterRequest, groups: List<UserGroup>): User {
        val app = request.app!!
        val device = request.device!!
        val screen = device.screen!!
        val os = device.os!!

        return User(
            id = idGenerator.generateId(),
            email = Email(request.email!!),
            roles = setOf(User.Role.MEMBER),
            fcmToken = request.fcmToken!!,
            clientId = request.clientId!!,
            profile = Profile(
                displayName = DisplayName(request.displayName!!),
                image = AvatarImage(Url(request.photoUrl!!)),
                gender = Gender.valueOf(request.gender!!.toUpperCase()),
                locale = UserLocale(language = request.language!!, country = request.country!!)
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

    private fun createCustomToken(userId: Long): String {
        val claims = mapOf(
            "roles" to listOf("MEMBER")
        )

        return firebaseAuth.createCustomToken(userId.toString(), claims)
    }

    private fun saveTx(
        user: User,
        filter: NanoCuckooFilter,
        followedUsers: Collection<User>,
        subscribedGroupIds: List<Long>
    ) {
        ofy().transact {
            saveEmailFilter(filter, user.email.value)

            ofy().save().entity(user)
        }
    }

    private fun saveEmailFilter(filter: NanoCuckooFilter, email: String) {
        filter.insert(email)
        memcacheService.put(User.KEY_FILTER_USER_IDENTIFIER, filter)
    }

    private fun getFollowedUsers(userIds: List<Long>): Collection<User> {
        return when {
            userIds.isEmpty() -> emptyList()
            else -> ofy().load().type(User::class.java).ids(userIds).values
        }
    }

    private fun getUserIdentityFilter(): NanoCuckooFilter {
        return memcacheService.get(User.KEY_FILTER_USER_IDENTIFIER) as NanoCuckooFilter
    }
}