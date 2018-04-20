package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.usecase.SearchUserUseCase
import com.yoloo.server.user.domain.usecase.contract.SearchUserUseCaseContract
import com.yoloo.server.user.infrastructure.mapper.SearchUserResponseMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchUserUseCaseImpl @Autowired constructor(
    private val searchUserResponseMapper: SearchUserResponseMapper
) : SearchUserUseCase {

    override fun execute(request: SearchUserUseCaseContract.Request): SearchUserUseCaseContract.Response {
        // TODO implement db logic

        val response = CollectionResponse.builder<SearchUserResponse>()
            .data(emptyList())
            .prevPageToken(request.cursor)
            .nextPageToken("")
            .build()

        return SearchUserUseCaseContract.Response(response)
    }
}