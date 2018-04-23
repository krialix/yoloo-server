package com.yoloo.server.post.domain.usecase.contract

import com.yoloo.server.post.domain.request.PostRequest
import com.yoloo.server.post.domain.response.PostResponse
import java.security.Principal

interface InsertPostContract {

    data class Request(val principal: Principal?, val postRequest: PostRequest)

    data class Response(val response: PostResponse)
}