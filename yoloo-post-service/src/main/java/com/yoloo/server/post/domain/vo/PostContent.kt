package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class PostContent private constructor(var value: String) {

    companion object {
        fun create(value: String): PostContent {
            return PostContent(value.trim())
        }
    }
}