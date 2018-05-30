package com.yoloo.server.group.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.ValueObject

@NoArg
data class Description(var value: String) : ValueObject<Description> {

    override fun sameValueAs(other: Description?): Boolean {
        return equals(other)
    }
}