package com.yoloo.server.comment.domain.usecase.contract

import com.yoloo.server.comment.domain.response.CommentResponse
import com.yoloo.server.common.response.attachment.CollectionResponse
import java.security.Principal

class ListCommentsContract {

    data class Request(val principal: Principal?, val postId: String, val cursor: String?)

    data class Response(val response: CollectionResponse<CommentResponse>)
}