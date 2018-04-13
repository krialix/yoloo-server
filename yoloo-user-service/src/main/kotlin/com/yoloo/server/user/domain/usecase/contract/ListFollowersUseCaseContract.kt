package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.response.FollowerResponse
import org.dialectic.jsonapi.response.DataResponse

interface ListFollowersUseCaseContract {

    data class Request(val userId: String, val limit: Int, val cursor: String?)

    data class Response(val response: DataResponse<FollowerResponse>)
}