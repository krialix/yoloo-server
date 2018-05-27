package com.yoloo.server.post.usecase

import com.yoloo.server.rest.error.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.ApprovedCommentId
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class ApproveCommentUseCase {
    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(!comment.approved, "comment.conflict_approve")

        val post = ofy().load().type(Post::class.java).id(comment.postId.value).now()

        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden_approve")

        comment.approved = true
        post.approvedCommentId = ApprovedCommentId(commentId)

        ofy().save().entities(post, comment)

        // TODO Send notification to commenter
    }
}