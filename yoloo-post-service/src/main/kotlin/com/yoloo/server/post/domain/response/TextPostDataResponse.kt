package com.yoloo.server.post.domain.response

import java.time.LocalDateTime

open class TextPostDataResponse(
    open var title: String,
    open var content: String,
    open var topic: PostTopicResponse,
    open var tags: List<String>,
    open var approvedCommentId: String?,
    open var bounty: Int,
    open var count: PostCountResponse,
    open var voteDir: Int,
    open var createdAt: LocalDateTime
) : PostDataResponse