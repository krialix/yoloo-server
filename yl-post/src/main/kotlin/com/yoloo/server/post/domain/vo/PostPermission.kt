package com.yoloo.server.post.domain.vo

enum class PostPermission {
    ALLOW_COMMENTING,
    ALLOW_VOTING,
    ALLOW_COMMENT_VOTING;

    companion object {
        fun default(): Set<PostPermission> {
            return setOf(
                ALLOW_COMMENTING,
                ALLOW_VOTING,
                ALLOW_COMMENT_VOTING
            )
        }
    }
}