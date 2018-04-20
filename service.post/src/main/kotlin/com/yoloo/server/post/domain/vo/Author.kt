package com.yoloo.server.post.domain.vo

import com.googlecode.objectify.annotation.Ignore
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class Author(
    @Index
    var id: String,

    var displayName: String,

    var avatarUrl: String,

    var url: String? = null,

    @Ignore
    var self: Boolean = false
)