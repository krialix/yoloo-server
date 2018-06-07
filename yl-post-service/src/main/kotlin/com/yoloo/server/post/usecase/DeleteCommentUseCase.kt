package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.rest.exception.ServiceExceptions
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class DeleteCommentUseCase(private val pubSubTemplate: PubSubTemplate, private val objectMapper: ObjectMapper) {

    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkForbidden(
            comment.author.id == requesterId,
            "comment.forbidden_delete"
        )
        ServiceExceptions.checkForbidden(!comment.approved, "comment.forbidden_delete_approved")

        val post = ofy().load().type(Post::class.java).id(comment.postId.value).now()

        post.countData.commentCount.dec()

        val voteKeys = getVoteKeysForComment(commentId)

        val pendingDeleteKeys = ImmutableList.builder<Key<*>>()
            .add(comment.key)
            .addAll(voteKeys)
            .build()

        val deleteFuture = ofy().delete().keys(pendingDeleteKeys)
        val saveFuture = ofy().save().entity(post)

        publishCommentDeletedEvent(comment)

        if (AppengineUtil.isTest()) {
            deleteFuture.now()
            saveFuture.now()
        }
    }

    private fun publishCommentDeletedEvent(comment: Comment) {
        val json = objectMapper.writeValueAsString(comment)
        pubSubTemplate.publish("comment.delete", json, null)
    }

    private fun getVoteKeysForComment(commentId: Long): List<Key<Vote>> {
        return ofy()
            .load()
            .type(Vote::class.java)
            .filter(Vote.INDEX_VOTABLE_ID, commentId)
            .keys()
            .list()
    }
}