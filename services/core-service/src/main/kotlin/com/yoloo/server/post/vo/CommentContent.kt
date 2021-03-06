package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class CommentContent(var value: String) {

    init {
        value = value.trim()
    }
}
