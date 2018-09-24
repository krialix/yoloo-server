package com.yoloo.server.post.usecase

import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Post
import com.yoloo.server.user.entity.User
import com.yoloo.server.like.entity.Like
import org.springframework.stereotype.Service

@Service
class DeleteCommentUseCase {

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

        ofy().delete().keys(pendingDeleteKeys)
        ofy().save().entities(post, user)
    }

    // TODO Batch deletion of like keys at the end of the day
    private fun getVoteKeysForComment(commentId: Long): List<Key<Like>> {
        return ofy()
            .load()
            .type(Like::class.java)
            .filter(Like.INDEX_LIKEABLE_ID, commentId)
            .keys()
            .list()
    }
}
