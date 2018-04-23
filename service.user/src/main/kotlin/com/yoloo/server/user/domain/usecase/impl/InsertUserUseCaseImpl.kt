package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.api.exception.ConflictException
import com.yoloo.server.common.util.TimestampIdGenerator
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.request.InsertUserRequest
import com.yoloo.server.user.domain.usecase.InsertUserUseCase
import com.yoloo.server.user.domain.usecase.contract.InsertUserContract
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import com.yoloo.server.user.infrastructure.social.RequestPayload
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProviderFactory
import com.yoloo.server.user.infrastructure.util.GroupInfoFetcher
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertUserUseCaseImpl(
    private val userResponseMapper: UserResponseMapper,
    private val userInfoProviderFactory: UserInfoProviderFactory,
    private val groupInfoFetcher: GroupInfoFetcher
) : InsertUserUseCase {

    override fun execute(request: InsertUserContract.Request): InsertUserContract.Response {
        val userRequest = request.insertUserRequest

        val userInfoProvider = userInfoProviderFactory.create(userRequest.providerType)
        val requestPayload = createRequestPayload(userRequest)
        val userInfo = userInfoProvider.getUserInfo(requestPayload)

        checkUserEmailExists(userInfo.email)

        val groups = groupInfoFetcher.fetch(userRequest.subscribedGroupIds)

        // todo increase group counts via user following group list
        // todo create user feed task and populate feed with group posts

        val user = User(
            displayName = UserDisplayName(value = userInfo.displayName),
            email = Email(userInfo.email),
            providerId = userInfo.providerId,
            image = AvatarImage(userInfo.picture),
            gender = Gender.valueOf(userRequest.gender.toUpperCase()),
            fcmToken = userRequest.fcmToken,
            scopes = setOf("user:read", "user:write", "post:read", "post:write"),
            lastKnownIP = IP(userRequest.lastKnownIP),
            locale = UserLocale(language = Locale.ENGLISH.language, country = "en_US"),
            subscribedGroups = groups
        )

        val response = userResponseMapper.apply(user)

        return InsertUserContract.Response(response)
    }

    private fun checkUserEmailExists(email: String) {
        val userKey = ofy().load()
            .type(User::class.java)
            .filter("email.value", email)
            .keys()
            .first()
            .now()

        if (userKey != null) {
            throw ConflictException("user.register.exists")
        }
    }

    private fun createRequestPayload(request: InsertUserRequest): RequestPayload {
        return RequestPayload(
            clientId = request.clientId,
            subscribedGroupIds = request.subscribedGroupIds,
            token = request.token,
            displayName = request.displayName,
            email = request.email,
            password = request.password
        )
    }
}