package com.yoloo.server.group.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.ValueObject

@NoArg
data class GroupCountData(
    var postCount: Int = 0,

    var subscriberCount: Int = 0
) : ValueObject<GroupCountData>