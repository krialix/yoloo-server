package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.response.UserResponse
import java.security.Principal

interface GetUserUseCaseContract {

    data class Request(val principal: Principal?, val userId: String)

    data class Response(val response: UserResponse)
}