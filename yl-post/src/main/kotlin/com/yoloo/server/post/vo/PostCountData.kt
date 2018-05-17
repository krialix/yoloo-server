package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.PositiveOrZero

@NoArg
data class PostCountData(
    @field:PositiveOrZero
    var commentCount: Int = 0,

    @field:PositiveOrZero
    var voteCount: Int = 0
)