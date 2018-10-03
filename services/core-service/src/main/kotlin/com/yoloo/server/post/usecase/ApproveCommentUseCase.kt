package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import org.springframework.stereotype.Service

@Service
class ApproveCommentUseCase(
    private val memcacheService: AsyncMemcacheService
) : AbstractUseCase<ApproveCommentUseCase.Input, Unit>() {

    override fun onExecute(input: Input) {
        /*val postKey = Post.createKey(input.postId)
        val commentKey = Comment.createKey(input.commentId)

        val map = ofy().load().keys(postKey, commentKey) as Map<*, *>

        val post = map[postKey] as Post?
        val comment = map[commentKey] as Comment?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(!comment!!.approved, "comment.conflict_approve")
        ServiceExceptions.checkForbidden(post!!.author.id == requesterId, "post.forbidden_approve")

        val commentUser = ofy().load().type(User::class.java).id(comment.author.id).now()

        comment.approve()
        post.approve(commentId)

        ofy().save().entities(post, comment)*/
    }

    /*fun execute(requesterId: Long, postId: Long, commentId: Long) {
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
    }*/

    private fun addToNotificationQueue(comment: Comment, commentAuthorFcmToken: String) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_COMMENT))
            .addData("id", comment.id.toString())
            .addData("postId", comment.postId.email.toString())
            .addData("commentAuthorFcmToken", commentAuthorFcmToken)
            .build()

        notificationQueueService.addQueueAsync(event)*/
    }

    data class Input(val requesterId: Long, val postId: Long, val commentId: Long)
}
