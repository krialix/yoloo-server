package com.yoloo.server.group.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage

@NoArg
data class Owner(
    @Index
    var id: Long,

    var displayName: String,

    var avatar: AvatarImage,

    var verified: Boolean
)