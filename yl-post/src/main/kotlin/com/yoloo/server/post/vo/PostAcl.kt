package com.yoloo.server.post.vo

enum class PostAcl {
    ALLOW_COMMENTING,
    ALLOW_VOTING,
    ALLOW_COMMENT_VOTING;

    companion object {
        fun default(): Set<PostAcl> {
            return setOf(
                ALLOW_COMMENTING,
                ALLOW_VOTING,
                ALLOW_COMMENT_VOTING
            )
        }
    }
}