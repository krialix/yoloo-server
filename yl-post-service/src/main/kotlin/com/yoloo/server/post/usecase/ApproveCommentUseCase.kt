package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppengineEnv
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.ApprovedCommentId
import com.yoloo.server.rest.exception.ServiceExceptions
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class ApproveCommentUseCase(private val objectMapper: ObjectMapper, private val pubSubTemplate: PubSubTemplate) {

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

        val saveFuture = ofy().save().entities(post, comment)
        if (AppengineEnv.isTest()) {
            saveFuture.now()
        }

        publishCommentApprovedEvent(comment)
    }

    private fun publishCommentApprovedEvent(comment: Comment) {
        val json = objectMapper.writeValueAsString(comment)
        pubSubTemplate.publish("comment.approved", json, null)
    }
}