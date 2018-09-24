package com.yoloo.server.post.vo

interface Commentable {

    fun incCommentCount()

    fun decCommentCount()

    fun isCommentingAllowed(): Boolean
}
