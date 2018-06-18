package com.yoloo.server.comment.usecase

import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.event.PubSubEvent
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.user.entity.User
import com.yoloo.server.vote.entity.Vote
import org.springframework.context.ApplicationEventPublisher

class DeleteCommentUseCase(private val eventPublisher: ApplicationEventPublisher) {

    fun execute(requesterId: Long, postId: Long, commentId: Long) {
        val postKey = Post.createKey(postId)
        val commentKey = Comment.createKey(commentId)
        val userKey = User.createKey(requesterId)

        val map = ofy().load().keys(postKey, commentKey, userKey) as Map<*, *>

        val post = map[postKey] as Post?
        val comment = map[commentKey] as Comment?
        val user = map[userKey] as User

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkForbidden(
            comment!!.author.id == requesterId,
            "comment.forbidden_delete"
        )
        ServiceExceptions.checkForbidden(!comment.approved, "comment.forbidden_delete_approved")

        post!!.countData.commentCount = post.countData.commentCount.dec()
        user.profile.countData.commentCount = user.profile.countData.commentCount.dec()

        val voteKeys = getVoteKeysForComment(commentId)

        val pendingDeleteKeys = ImmutableList.builder<Key<*>>()
            .add(comment.key)
            .addAll(voteKeys)
            .build()

        val deleteResult = ofy().delete().keys(pendingDeleteKeys)
        val saveResult = ofy().save().entities(post, user)
        TestUtil.saveResultsNowIfTest(deleteResult, saveResult)

        eventPublisher.publishEvent(PubSubEvent("comment.delete", comment, this))
    }

    // TODO Batch deletion of vote keys at the end of the day
    private fun getVoteKeysForComment(commentId: Long): List<Key<Vote>> {
        return ofy()
            .load()
            .type(Vote::class.java)
            .filter(Vote.INDEX_VOTABLE_ID, commentId)
            .keys()
            .list()
    }
}