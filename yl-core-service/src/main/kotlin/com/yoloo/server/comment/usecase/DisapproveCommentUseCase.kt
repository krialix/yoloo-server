package com.yoloo.server.comment.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post

class DisapproveCommentUseCase {

    fun execute(requesterId: Long, postId: Long, commentId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val commentKey = Key.create(Comment::class.java, commentId)

        val map = ofy().load().keys(postKey, commentKey) as Map<*, *>

        val post = map[postKey] as Post?
        val comment = map[commentKey] as Comment?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(comment!!.approved, "comment.not_found_approve")
        ServiceExceptions.checkForbidden(post!!.author.id == requesterId, "post.forbidden_disapprove")

        comment.approved = false
        post.approvedCommentId = null

        val saveResult = ofy().save().entities(post, comment)
        TestUtil.saveResultsNowIfTest(saveResult)
    }
}