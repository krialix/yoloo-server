package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserInfoResponse(
    val id: Long,
    val self: Boolean,
    val displayName: String,
    val image: String,
    val verified: Boolean
)