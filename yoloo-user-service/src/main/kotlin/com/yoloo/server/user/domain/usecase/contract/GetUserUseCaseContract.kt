package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.response.UserResponse
import org.dialectic.jsonapi.response.DataResponse

interface GetUserUseCaseContract {

    data class Request(val userId: String)

    data class Response(val response: DataResponse<UserResponse>)
}