package com.yoloo.server.user.domain.request

import com.yoloo.server.common.usecase.Request

data class GetUserRequest(val userId: String) : Request