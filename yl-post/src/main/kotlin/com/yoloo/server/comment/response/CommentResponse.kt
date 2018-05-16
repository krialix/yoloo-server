package com.yoloo.server.comment.response

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.response.AuthorResponse
import java.time.LocalDateTime

@NoArg
data class CommentResponse(
    val id: Long,
    val author: AuthorResponse,
    val content: String,
    val approved: Boolean,
    val voteCount: Int,
    val createdAt: LocalDateTime
)