package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.request.PatchUserRequest

interface PatchUserUseCaseContract {

    data class Request(val userId: String, val patchUserRequest: PatchUserRequest)
}