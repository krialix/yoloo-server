package com.yoloo.server.post.vo.postdataresponse

import com.yoloo.server.post.vo.PostAttachmentResponse
import com.yoloo.server.post.vo.PostCountResponse
import com.yoloo.server.post.vo.PostGroupResponse
import java.time.LocalDateTime

data class RichPostDataResponse(
    override var title: String,
    override var group: PostGroupResponse,
    override var tags: List<String>,
    override var approvedCommentId: Long?,
    override var bounty: Int,
    override var count: PostCountResponse,
    override var voted: Boolean,
    override var bookmarked: Boolean,
    override var createdAt: LocalDateTime,
    val attachments: List<PostAttachmentResponse>
) : TextPostDataResponse(
    title,
    group,
    tags,
    approvedCommentId,
    bounty,
    count,
    voted,
    bookmarked,
    createdAt
)