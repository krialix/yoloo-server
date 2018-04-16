package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.request.PatchUserRequest
import java.security.Principal

interface PatchUserUseCaseContract {

    data class Request(val principal: Principal, val userId: String, val patchUserRequest: PatchUserRequest)
}