package com.yoloo.server.comment.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.vo.ApprovedCommentId
import com.yoloo.server.common.event.PubSubEvent
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.context.ApplicationEventPublisher

class ApproveCommentUseCase(private val eventPublisher: ApplicationEventPublisher) {

    fun execute(requesterId: Long, postId: Long, commentId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val commentKey = Key.create(Comment::class.java, commentId)

        val map = ofy().load().keys(postKey, commentKey) as Map<*, *>

        val post = map[postKey] as Post?
        val comment = map[commentKey] as Comment?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkBadRequest(!comment!!.approved, "comment.conflict_approve")
        ServiceExceptions.checkForbidden(post!!.author.id == requesterId, "post.forbidden_approve")

        comment.approved = true
        post.approvedCommentId = ApprovedCommentId(commentId)

        val saveResult = ofy().save().entities(post, comment)
        TestUtil.saveResultsNowIfTest(saveResult)

        eventPublisher.publishEvent(PubSubEvent("comment.approved", comment, this))
    }
}