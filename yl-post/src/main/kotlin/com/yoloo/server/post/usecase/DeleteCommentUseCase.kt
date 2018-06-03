package com.yoloo.server.post.usecase

import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.rest.exception.ServiceExceptions

class DeleteCommentUseCase {

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

        ofy().delete().keys(pendingDeleteKeys)
        ofy().save().entity(post)
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