package com.yoloo.server.comment.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class CommentContent constructor(var value: String) {

    init {
        value = value.trim()
    }
}