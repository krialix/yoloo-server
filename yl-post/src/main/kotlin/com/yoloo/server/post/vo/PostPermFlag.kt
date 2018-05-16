package com.yoloo.server.post.vo

import java.util.*

enum class PostPermFlag {
    ALLOW_COMMENTING,
    ALLOW_VOTING,
    ALLOW_COMMENT_VOTING;

    companion object {
        fun default(): EnumSet<PostPermFlag> {
            return EnumSet.of(
                ALLOW_COMMENTING,
                ALLOW_VOTING,
                ALLOW_COMMENT_VOTING
            )
        }
    }
}