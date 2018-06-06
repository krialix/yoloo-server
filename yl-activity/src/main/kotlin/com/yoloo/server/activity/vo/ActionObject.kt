package com.yoloo.server.activity.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.ValueObject

@NoArg
data class ActionObject(
    @Index
    var id: Long
) : ValueObject<ActionObject>