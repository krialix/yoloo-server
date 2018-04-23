package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.request.InsertUserRequest
import com.yoloo.server.user.domain.response.UserResponse

interface InsertUserContract {

    data class Request(val insertUserRequest: InsertUserRequest)

    data class Response(val response: UserResponse)
}