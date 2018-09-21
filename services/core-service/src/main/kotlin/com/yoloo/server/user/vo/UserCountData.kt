package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserCountData(
    var postCount: Int = 0,

    var commentCount: Int = 0,

    var followingCount: Long = 0,

    var followerCount: Long = 0
)