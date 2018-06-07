package com.yoloo.server.post.usecase

import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.rest.exception.ServiceExceptions

class DisapproveCommentUseCase {

    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(comment.approved, "comment.not_found_approve")

        val post = ofy().load().type(Post::class.java).id(comment.postId.value).now()

        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden_approve")

        comment.approved = false
        post.approvedCommentId = null

        val saveFuture = ofy().save().entities(post, comment)
        if (AppengineUtil.isTest()) {
            saveFuture.now()
        }

        // TODO Send notification to commenter
    }
}