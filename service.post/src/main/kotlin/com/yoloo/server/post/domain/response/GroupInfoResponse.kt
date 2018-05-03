package com.yoloo.server.post.domain.response

import com.yoloo.server.common.util.NoArg

@NoArg
data class GroupInfoResponse(val id: Long, val displayName: String)