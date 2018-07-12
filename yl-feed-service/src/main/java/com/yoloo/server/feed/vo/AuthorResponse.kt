package com.yoloo.server.feed.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class AuthorResponse(val id: Long, val self: Boolean, val displayName: String, val profileImageUrl: String)