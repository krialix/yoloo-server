package com.yoloo.server.post.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage

@NoArg
data class Author(
    @Index
    var id: Long,

    var displayName: String,

    var avatar: AvatarImage,

    var verified: Boolean
)