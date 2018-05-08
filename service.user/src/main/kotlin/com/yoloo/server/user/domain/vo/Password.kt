package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Password(var value: String) {

    init {
        value = value.trim()
    }
}