package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class DisplayName(var value: String) {

    val slug
        get() = value.replace(" ", "").toLowerCase()
}