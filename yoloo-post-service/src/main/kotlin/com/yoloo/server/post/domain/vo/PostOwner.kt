package com.yoloo.server.post.domain.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class PostOwner(
    @Index
    var userId: String,

    var displayName: String,

    var avatarUrl: String
)