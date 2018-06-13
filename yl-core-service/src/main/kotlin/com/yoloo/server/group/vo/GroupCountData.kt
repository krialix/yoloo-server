package com.yoloo.server.group.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class GroupCountData(
    var postCount: Int = 0,

    var subscriberCount: Int = 0
)