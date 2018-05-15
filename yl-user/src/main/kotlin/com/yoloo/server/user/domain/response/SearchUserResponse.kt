package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg

@NoArg
data class SearchUserResponse(val id: String, val displayName: String, val avatarUrl: String)