package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import java.time.Instant

@NoArg
data class PostResponse(
    val id: Long,
    val author: AuthorResponse,
    val title: String,
    val content: String,
    val group: PostGroupResponse,
    val tags: List<String>,
    val approvedCommentId: Long?,
    val bounty: Int,
    val count: PostCountResponse,
    val voted: Boolean,
    val bookmarked: Boolean,
    val createdAt: Instant,
    val medias: List<MediaResponse>
)
