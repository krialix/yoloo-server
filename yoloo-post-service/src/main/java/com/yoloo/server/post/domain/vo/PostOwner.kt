package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class PostOwner(var userId: String, var avatarUrl: String)