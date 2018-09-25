package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class AuthorResponse(val id: String, val self: Boolean, val displayName: String, val profileImageUrl: String)
