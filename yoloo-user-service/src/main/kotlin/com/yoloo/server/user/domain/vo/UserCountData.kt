package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.PositiveOrZero

@NoArg
data class UserCountData(
    @field:PositiveOrZero
    private var postCount: Int = 0,

    @field:PositiveOrZero
    private var commentCount: Int = 0,

    @field:PositiveOrZero
    private var coinCount: Int = 0
)