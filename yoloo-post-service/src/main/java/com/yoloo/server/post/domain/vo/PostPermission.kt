package com.yoloo.server.post.domain.vo

enum class PostPermission {
    ALLOW_COMMENTING,
    ALLOW_VOTING,
    ALLOW_COMMENT_VOTING;

    companion object {
        fun defaultPermissions(): Set<PostPermission> {
            return setOf(
                PostPermission.ALLOW_COMMENTING,
                PostPermission.ALLOW_VOTING,
                PostPermission.ALLOW_COMMENT_VOTING
            )
        }
    }
}