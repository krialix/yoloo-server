package com.yoloo.server.post.domain.response.postdata

import com.yoloo.server.post.domain.response.PostAttachmentResponse
import com.yoloo.server.post.domain.response.PostCountResponse
import com.yoloo.server.post.domain.response.PostTopicResponse
import java.time.LocalDateTime

data class RichPostDataResponse(
    override var title: String,
    override var topic: PostTopicResponse,
    override var tags: List<String>,
    override var approvedCommentId: String?,
    override var bounty: Int,
    override var count: PostCountResponse,
    override var voteDir: Int,
    override var createdAt: LocalDateTime,
    val attachments: List<PostAttachmentResponse>
) : TextPostDataResponse(title, topic, tags, approvedCommentId, bounty, count, voteDir, createdAt)