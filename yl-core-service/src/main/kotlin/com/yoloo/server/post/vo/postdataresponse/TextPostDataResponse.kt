package com.yoloo.server.post.vo.postdataresponse

import com.yoloo.server.post.vo.PostCountResponse
import com.yoloo.server.post.vo.PostGroupResponse
import java.time.LocalDateTime

open class TextPostDataResponse(
    open var title: String,
    open var group: PostGroupResponse,
    open var tags: List<String>,
    open var approvedCommentId: Long?,
    open var bounty: Int,
    open var count: PostCountResponse,
    open var voted: Boolean,
    open var bookmarked: Boolean,
    open var createdAt: LocalDateTime
) : PostDataResponse