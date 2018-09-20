package com.yoloo.server.comment.usecase

import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Post
import com.yoloo.server.user.entity.User
import org.springframework.stereotype.Service

@Service
class ApproveCommentUseCase {

    fun execute(requesterId: Long, postId: Long, commentId: Long) {
        val postKey = Post.createKey(postId)
        val commentKey = Comment.createKey(commentId)

        val map = ofy().load().keys(postKey, commentKey) as Map<*, *>

        val post = map[postKey] as Post?
        val comment = map[commentKey] as Comment?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(!comment!!.approved, "comment.conflict_approve")
        ServiceExceptions.checkForbidden(post!!.author.id == requesterId, "post.forbidden_approve")

        val commentUser = ofy().load().type(User::class.java).id(comment.author.id).now()

        comment.approve()
        post.approve(commentId)

        ofy().save().entities(post, comment)

        addToNotificationQueue(comment, commentUser.fcmToken)
    }

    private fun addToNotificationQueue(comment: Comment, commentAuthorFcmToken: String) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_COMMENT))
            .addData("id", comment.id.toString())
            .addData("postId", comment.postId.value.toString())
            .addData("commentAuthorFcmToken", commentAuthorFcmToken)
            .build()

        notificationQueueService.addQueueAsync(event)*/
    }
}
