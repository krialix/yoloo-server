package com.yoloo.server.activity.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.ValueObject

@NoArg
data class Actor(
    @Index
    var id: Long,
    var displayName: String,
    var image: AvatarImage
) : ValueObject<Actor>