package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class GroupInfoResponse(val id: Long, val displayName: String)