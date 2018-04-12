package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.FollowingResponse
import com.yoloo.server.user.domain.usecase.ListFollowingsUseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowingsUseCaseContract
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListFollowingsUseCaseImpl @Autowired constructor(
    private val followingResponseMapper: Mapper<User, FollowingResponse>
) : ListFollowingsUseCase {

    override fun execute(request: ListFollowingsUseCaseContract.Request): ListFollowingsUseCaseContract.Response {
        // TODO implement db logic

        val data = JsonApi.data(emptyList<FollowingResponse>())

        return ListFollowingsUseCaseContract.Response(data)
    }
}