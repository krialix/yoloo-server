package com.yoloo.server.comment.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.googlecode.objectify.Key
import com.yoloo.server.BaseUseCase
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.vo.ApprovedCommentId
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class ApproveCommentUseCase(
    objectMapper: ObjectMapper,
    pubSubTemplate: PubSubTemplate
) : BaseUseCase(objectMapper, pubSubTemplate) {

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
        putResult(saveFuture)

        publishEvent("comment.approved", comment)
    }
}