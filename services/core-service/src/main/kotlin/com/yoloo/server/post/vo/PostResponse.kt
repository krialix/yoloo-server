package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.usecase.UseCase
import java.time.Instant

@NoArg
data class PostResponse(
    val id: String,
    val author: AuthorResponse2,
    val title: String,
    val content: String,
    val group: PostGroupResponse,
    val tags: List<String>,
    val approvedCommentId: String?,
    val bounty: Int,
    val count: PostCountResponse,
    val liked: Boolean,
    val bookmarked: Boolean,
    val createdAt: Instant,
    val medias: List<MediaResponse>
) : UseCase.Output
