package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Password(var value: String) {

    init {
        value = value.trim()
    }
}