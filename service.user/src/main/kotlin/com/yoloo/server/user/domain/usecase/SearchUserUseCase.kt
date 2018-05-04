package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.infrastructure.mapper.SearchUserResponseMapper
import org.springframework.stereotype.Component

@Component
class SearchUserUseCase(
    private val searchUserResponseMapper: SearchUserResponseMapper
) : UseCase<SearchUserUseCase.Request, CollectionResponse<SearchUserResponse>> {

    override fun execute(request: Request): CollectionResponse<SearchUserResponse> {
        val response = CollectionResponse.builder<SearchUserResponse>()
            .data(emptyList())
            .prevPageToken(request.cursor)
            .nextPageToken("")
            .build()

        return response
    }

    class Request(val query: String, val cursor: String?)
}