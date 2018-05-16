package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class RelationshipResponse(val id: Long, val displayName: String, val avatarUrl: String)