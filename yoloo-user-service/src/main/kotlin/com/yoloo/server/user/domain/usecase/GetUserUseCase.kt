package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.request.GetUserRequest
import com.yoloo.server.user.domain.response.GetUserResponse
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetUserUseCase @Autowired constructor(private val userResponseMapper: UserResponseMapper) :
    UseCase<GetUserRequest, GetUserResponse> {

    override fun execute(request: GetUserRequest): GetUserResponse {
        val user = ofy().load().type(User::class.java).id(request.userId).now()

        if (user == null || user.isDeleted()) {
            throw NotFoundException("user.error.not-found")
        }

        val data = userResponseMapper.apply(user)
        val dataResponse = JsonApi.data(data)

        return GetUserResponse(dataResponse)
    }
}