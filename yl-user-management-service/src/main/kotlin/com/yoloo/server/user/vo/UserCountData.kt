package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.ValueObject

@NoArg
data class UserCountData(
    var postCount: Int = 0,

    var commentCount: Int = 0,

    var followingCount: Long = 0,

    var followerCount: Long = 0
) : ValueObject<UserCountData>