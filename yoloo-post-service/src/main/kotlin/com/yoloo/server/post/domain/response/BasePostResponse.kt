package com.yoloo.server.post.domain.response

abstract class BasePostResponse(
    val id: String,
    val type: String,
    val author: AuthorResponse,
    val content: PostContentResponse
)