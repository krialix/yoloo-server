package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class SearchUserResponse(val id: String, val displayName: String, val avatarUrl: String)