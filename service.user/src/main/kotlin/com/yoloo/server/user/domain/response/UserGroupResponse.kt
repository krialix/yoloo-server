package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserGroupResponse(val id: Long, val displayName: String, val imageUrl: String)