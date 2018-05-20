package com.yoloo.server.comment.usecase

import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.ApprovedCommentId
import org.springframework.stereotype.Component

@Component
class ApproveCommentUseCase {
    fun execute(requesterId: Long, commentId: Long) {
        var comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not-found")
        ServiceExceptions.checkBadRequest(!comment.approved, "comment.already-approved")

        var post = ofy().load().type(Post::class.java).id(comment.postId.value).now()

        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.not_allowed_approve")
        ServiceExceptions.checkBadRequest(post.approvedCommentId == null, "comment.already-approved")

        comment = comment.copy(approved = true)
        post = post.copy(approvedCommentId = ApprovedCommentId(commentId))

        ofy().save().entities(post, comment)

        // TODO Send notification to commenter
    }
}