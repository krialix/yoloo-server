package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.PositiveOrZero

@NoArg
data class UserCountData(
    @field:PositiveOrZero
    var postCount: Int = 0,

    @field:PositiveOrZero
    var commentCount: Int = 0,

    @field:PositiveOrZero
    var coinCount: Int = 0,

    @field:PositiveOrZero
    var followingCount: Long = 0,

    @field:PositiveOrZero
    var followerCount: Long = 0
)