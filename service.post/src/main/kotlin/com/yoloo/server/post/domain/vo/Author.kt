package com.yoloo.server.post.domain.vo

import com.googlecode.objectify.annotation.Ignore
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url

@NoArg
data class Author(
    @Index
    var id: Long,

    var displayName: String,

    var avatarUrl: String,

    var url: Url? = null,

    @Ignore
    var self: Boolean = false
)