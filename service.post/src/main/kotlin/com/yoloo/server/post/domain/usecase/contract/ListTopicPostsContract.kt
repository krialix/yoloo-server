package com.yoloo.server.post.domain.usecase.contract

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.post.domain.response.PostResponse
import java.security.Principal

interface ListTopicPostsContract {

    data class Request(val principal: Principal?, val topicId: String, val cursor: String?)

    data class Response(val response: CollectionResponse<PostResponse>)
}