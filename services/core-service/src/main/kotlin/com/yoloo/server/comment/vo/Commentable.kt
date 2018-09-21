package com.yoloo.server.comment.vo

interface Commentable {

    fun incCommentCount()

    fun decCommentCount()

    fun isCommentingAllowed(): Boolean
}
