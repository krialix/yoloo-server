package com.yoloo.server.user.usecase

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.user.vo.SearchUserResponse

class SearchUserUseCase {

    fun execute(query: String, cursor: String?): CollectionResponse<SearchUserResponse> {
        return CollectionResponse.builder<SearchUserResponse>()
            .data(emptyList())
            .prevPageToken(cursor)
            .nextPageToken("")
            .build()
    }
}
