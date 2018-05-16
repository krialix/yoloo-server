package com.yoloo.server.user.usecase

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.user.vo.SearchUserResponse
import org.springframework.stereotype.Component

@Component
class SearchUserUseCase : UseCase<SearchUserUseCase.Params, CollectionResponse<SearchUserResponse>> {

    override fun execute(params: Params): CollectionResponse<SearchUserResponse> {

        return CollectionResponse.builder<SearchUserResponse>()
            .data(emptyList())
            .prevPageToken(params.cursor)
            .nextPageToken("")
            .build()
    }

    class Params(val query: String, val cursor: String?)
}