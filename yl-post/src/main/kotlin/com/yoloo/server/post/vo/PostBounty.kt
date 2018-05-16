package com.yoloo.server.post.vo

import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.condition.IfNotZero
import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.PositiveOrZero

@NoArg
data class PostBounty(
    @field:PositiveOrZero
    @Index(IfNotZero::class)
    var value: Int = 0
)