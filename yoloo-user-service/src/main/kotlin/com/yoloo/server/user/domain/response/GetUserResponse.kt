package com.yoloo.server.user.domain.response

import com.yoloo.server.common.usecase.Response
import org.dialectic.jsonapi.response.DataResponse

data class GetUserResponse(val response: DataResponse<UserResponse>) : Response