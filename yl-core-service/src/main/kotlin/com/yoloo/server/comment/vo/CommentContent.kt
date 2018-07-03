package com.yoloo.server.comment.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class CommentContent constructor(var value: String) {

    init {
        value = value.trim()
    }
}