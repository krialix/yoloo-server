package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.usecase.SearchUserUseCase
import com.yoloo.server.user.domain.usecase.contract.SearchUserUseCaseContract
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchUserUseCaseImpl @Autowired constructor(
    private val searchUserResponseMapper: Mapper<User, SearchUserResponse>
) : SearchUserUseCase {

    override fun execute(request: SearchUserUseCaseContract.Request): SearchUserUseCaseContract.Response {
        // TODO implement db logic

        val data = JsonApi.data(emptyList<SearchUserResponse>())

        return SearchUserUseCaseContract.Response(data)
    }
}