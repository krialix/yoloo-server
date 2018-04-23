package com.yoloo.server.comment.domain.response

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.response.AuthorResponse
import java.time.LocalDateTime

@NoArg
data class CommentResponse(
    val id: String,
    val author: AuthorResponse,
    val content: String,
    val approved: Boolean,
    val likeCount: Int,
    val createdAt: LocalDateTime
)