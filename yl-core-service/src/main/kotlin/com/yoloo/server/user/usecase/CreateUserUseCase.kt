package com.yoloo.server.user.usecase

import com.google.api.core.ApiFuture
import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.firebase.auth.FirebaseAuth
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.service.NotificationQueueService
import com.yoloo.server.common.queue.service.SearchQueueService
import com.yoloo.server.common.queue.vo.EventType
import com.yoloo.server.common.queue.vo.YolooEvent
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Ip
import com.yoloo.server.common.vo.Url
import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.provider.EmailUserRecordProvider
import com.yoloo.server.user.provider.FacebookUserRecordProvider
import com.yoloo.server.user.provider.GoogleUserRecordProvider
import com.yoloo.server.user.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.lang.IllegalArgumentException

class CreateUserUseCase(
    @Qualifier(IdBeanQualifier.CACHED) private val idGenerator: LongIdGenerator,
    private val firebaseAuth: FirebaseAuth,
    private val memcacheService: AsyncMemcacheService,
    private val passwordEncoder: PasswordEncoder,
    private val userResponseMapper: UserResponseMapper,
    private val notificationQueueService: NotificationQueueService,
    private val searchQueueService: SearchQueueService
) {

    fun execute(request: UserCreateRequest): ResponseEntity<UserResponse> {
        val cacheMap = memcacheService.getAll(
            listOf(
                User.KEY_FILTER_USER_IDENTIFIER,
                Subscription.KEY_FILTER_SUBSCRIPTION,
                Relationship.KEY_FILTER_RELATIONSHIP
            )
        ).get()

        val userIdentityFilter = cacheMap[User.KEY_FILTER_USER_IDENTIFIER] as NanoCuckooFilter
        val subcriptionFilter = cacheMap[Subscription.KEY_FILTER_SUBSCRIPTION] as NanoCuckooFilter
        val relationshipFilter = cacheMap[Relationship.KEY_FILTER_RELATIONSHIP] as NanoCuckooFilter

        ServiceExceptions.checkConflict(!userIdentityFilter.contains(User.KEY_FILTER_USER_IDENTIFIER), "user.conflict")

        createFirebaseUser(request)

        val subscribedGroups = ofy()
            .load()
            .type(Group::class.java)
            .ids(request.subscribedGroupIds)
            .values
            .toList()

        val followedUsers = getFollowedUsers(request.followedUserIds.orEmpty())

        val user = createDbUser(request, subscribedGroups, followedUsers.size)
        val accessTokenFuture = createCustomToken(user.id)

        val updatedGroups = subscribedGroups
            .asSequence()
            .map { it.countData.subscriberCount.inc() }
            .asIterable()

        val subscriptions = subscribedGroups
            .asSequence()
            .map { Subscription.create(user.id, it.id, user.profile.displayName.value, user.profile.image.url.value) }
            .toList()

        val updatedUsers = followedUsers
            .asSequence()
            .map { it.profile.countData.followerCount.inc() }
            .toList()

        val relationships = followedUsers
            .asSequence()
            .map { Relationship.create(user.id, user.profile.displayName, user.profile.image, it.id) }
            .toList()

        saveTx(user, userIdentityFilter)

        val userResponse = userResponseMapper.apply(user, true, false)


        val httpHeaders = HttpHeaders()
        httpHeaders.add("AccessToken", accessTokenFuture.get())
        return ResponseEntity(userResponse, httpHeaders, HttpStatus.CREATED)
    }

    private fun createFirebaseUser(request: UserCreateRequest) {
        val userRecordProvider = when (request.providerId) {
            "google" -> GoogleUserRecordProvider(idGenerator)
            "facebook" -> FacebookUserRecordProvider(idGenerator)
            "email" -> EmailUserRecordProvider(idGenerator, passwordEncoder)
            else -> throw IllegalArgumentException("Given provider id is not valid")
        }

        val (userRecord, importOptions) = userRecordProvider.provide(request)
        firebaseAuth.importUsersAsync(listOf(userRecord), importOptions)
    }

    private fun createDbUser(request: UserCreateRequest, groups: List<Group>, followingCount: Int): User {
        val app = request.app!!
        val device = request.device!!
        val screen = device.screen!!
        val os = device.os!!
        val countData = when (followingCount) {
            0 -> UserCountData()
            else -> UserCountData(followingCount = followingCount.toLong())
        }

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
                locale = UserLocale(language = request.language!!, country = request.country!!),
                countData = countData
            ),
            subscribedGroups = groups.map { UserGroup(it.id, it.imageUrl.value, it.displayName.value) },
            appInfo = AppInfo(
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
                localIp = Ip(device.localIp!!),
                os = Os(
                    type = Os.Type.valueOf(os.type!!.toUpperCase()),
                    version = Os.Version(os.version!!)
                )
            )
        )
    }

    private fun createCustomToken(userId: Long): ApiFuture<String> {
        val claims = mapOf(
            "roles" to listOf("MEMBER")
        )

        return firebaseAuth.createCustomTokenAsync(userId.toString(), claims)
    }

    private fun saveTx(
        user: User,
        filter: NanoCuckooFilter
    ) {
        ofy().transact {
            saveEmailFilter(filter, user.email.value)

            addToSearchQueue(user)

            val saveResult = ofy().save().entity(user)
            TestUtil.saveNow(saveResult)
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

    private fun addToSearchQueue(user: User) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_USER))
            .addData("id", user.id.toString())
            .addData("displayName", user.profile.displayName.value)
            .build()

        searchQueueService.addQueueAsync(event)
    }

    private fun addToNotificationQueue(user: User) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_USER))
            .addData("id", user.id.toString())
            .addData("displayName", user.profile.displayName.value)
            .build()

        searchQueueService.addQueueAsync(event)
    }
}