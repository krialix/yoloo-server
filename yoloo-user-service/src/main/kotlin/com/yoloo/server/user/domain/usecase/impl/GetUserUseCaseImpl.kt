package com.yoloo.server.user.domain.usecase.impl

import com.googlecode.objectify.Key
import com.yoloo.server.common.Mapper
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.contract.GetUserUseCaseContract
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetUserUseCaseImpl @Autowired constructor(
    private val userResponseMapper: Mapper<User, UserResponse>
) : GetUserUseCase {

    override fun execute(request: GetUserUseCaseContract.Request): GetUserUseCaseContract.Response {
        val key = Key.create(User::class.java, request.userId)
        val user = ofy().load().key(key).now()

        if (user == null || user.isDeleted()) {
            throw NotFoundException("user.error.not-found")
        }

        val response = userResponseMapper.apply(user)
        val data = JsonApi.data(response)

        return GetUserUseCaseContract.Response(data)
    }
}