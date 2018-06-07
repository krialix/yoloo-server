package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class PostContent constructor(var value: String) {

    init {
        value = value.trim()
    }
}