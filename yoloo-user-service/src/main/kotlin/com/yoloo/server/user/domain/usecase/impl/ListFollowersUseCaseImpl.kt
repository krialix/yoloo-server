package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.FollowerResponse
import com.yoloo.server.user.domain.usecase.ListFollowersUseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowersUseCaseContract
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListFollowersUseCaseImpl @Autowired constructor(
    private val followerResponseMapper: Mapper<User, FollowerResponse>
) : ListFollowersUseCase {

    override fun execute(request: ListFollowersUseCaseContract.Request): ListFollowersUseCaseContract.Response {
        // TODO implement db logic

        val data = JsonApi.data(emptyList<FollowerResponse>())

        return ListFollowersUseCaseContract.Response(data)
    }
}