package com.yoloo.server.post.usecase

import com.yoloo.server.post.entity.Comment
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.stereotype.Component

@Component
class DisapproveCommentUseCase {
    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(comment.approved, "comment.not_found_approve")

        val post = ofy().load().type(Post::class.java).id(comment.postId.value).now()

        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden_approve")

        comment.approved = false
        post.approvedCommentId = null

        ofy().save().entities(post, comment)

        // TODO Send notification to commenter
    }
}