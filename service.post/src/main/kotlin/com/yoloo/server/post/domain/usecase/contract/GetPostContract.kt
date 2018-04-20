package com.yoloo.server.post.domain.usecase.contract

import com.yoloo.server.post.domain.response.PostResponse
import java.security.Principal

interface GetPostContract {

    data class Request(val principal: Principal?, val postId: String)

    data class Response(val response: PostResponse)
}