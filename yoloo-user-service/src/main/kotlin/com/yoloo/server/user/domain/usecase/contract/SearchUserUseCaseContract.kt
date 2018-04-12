package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.response.SearchUserResponse
import org.dialectic.jsonapi.response.DataResponse

interface SearchUserUseCaseContract {

    data class Request(val query: String)

    data class Response(val response: DataResponse<SearchUserResponse>)
}