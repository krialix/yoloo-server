package com.yoloo.server.post.vo

interface AuthorResponse2 {
    fun getId(): String

    fun isSelf(): Boolean

    fun getDisplayName(): String

    fun getProfileImageUrl(): String
}
