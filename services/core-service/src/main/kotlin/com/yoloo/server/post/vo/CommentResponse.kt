package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import java.time.Instant

@NoArg
data class CommentResponse(
    val id: String,
    val author: AuthorResponse,
    val content: String,
    val approved: Boolean,
    val liked: Boolean,
    val likeCount: Int,
    val createdAt: Instant
)
