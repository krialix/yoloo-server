package com.yoloo.server.comment.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.yoloo.server.BaseUseCase
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class DeleteCommentUseCase(
    pubSubTemplate: PubSubTemplate,
    objectMapper: ObjectMapper
) : BaseUseCase(objectMapper, pubSubTemplate) {

    fun execute(requesterId: Long, postId: Long, commentId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val commentKey = Key.create(Comment::class.java, commentId)
        val map = ofy().load().keys(postKey, commentKey) as Map<*, *>

        val post = map[postKey] as Post?
        val comment = map[commentKey] as Comment?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkForbidden(
            comment!!.author.id == requesterId,
            "comment.forbidden_delete"
        )
        ServiceExceptions.checkForbidden(!comment.approved, "comment.forbidden_delete_approved")

        post!!.countData.commentCount = post.countData.commentCount.dec()

        val voteKeys = getVoteKeysForComment(commentId)

        val pendingDeleteKeys = ImmutableList.builder<Key<*>>()
            .add(comment.key)
            .addAll(voteKeys)
            .build()

        val deleteFuture = ofy().delete().keys(pendingDeleteKeys)
        val saveFuture = ofy().save().entity(post)

        publishEvent("comment.delete", comment)

        putResult(deleteFuture, saveFuture)
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