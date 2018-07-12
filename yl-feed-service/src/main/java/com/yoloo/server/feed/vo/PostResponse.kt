package com.yoloo.server.feed.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.response.PostResponseData
import java.time.LocalDateTime

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
    val createdAt: LocalDateTime,
    val medias: List<MediaResponse>
) : PostResponseData