package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.user.domain.response.SearchUserResponse

interface SearchUserContract {

    data class Request(val query: String, val cursor: String?)

    data class Response(val response: CollectionResponse<SearchUserResponse>)
}