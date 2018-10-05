package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.Exceptions.checkException
import com.yoloo.server.filter.FilterService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.util.CommentErrors
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.spring.autoconfiguration.appengine.services.counter.CounterService
import org.springframework.stereotype.Service
import org.zalando.problem.Status

@Service
class DeleteCommentUseCase(
    private val filterService: FilterService,
    private val counterService: CounterService,
    private val hashids: Hashids
) : AbstractUseCase<DeleteCommentUseCase.Input, Unit>() {

    override fun onExecute(input: Input) {
        val commentHashId = hashids.decode(input.commentId)

        val commentId = commentHashId[0]
        val commentAuthorId = commentHashId[1]
        val commentPostId = commentHashId[2]

        val entityCache = filterService.getAll()

        checkException(entityCache.contains(commentId), Status.NOT_FOUND, CommentErrors.NOT_FOUND)
        checkException(commentAuthorId == input.requesterId, Status.FORBIDDEN, CommentErrors.FORBIDDEN)
        checkException(!entityCache.contains("approved:$commentId"), Status.FORBIDDEN, CommentErrors.FORBIDDEN_APPROVED)

        val commentKey = Comment.createKey(commentId)

        val likeKeys = getLikeKeysForComment(commentId)

        val pendingDeleteKeys = ImmutableList.builder<Key<*>>()
            .add(commentKey)
            .addAll(likeKeys)
            .build()

        ofy().defer().delete().keys(pendingDeleteKeys)

        counterService.decrement("POST_COMMENT:$commentPostId", "USER_COMMENT:$commentAuthorId")
    }

    // TODO Batch deletion of like keys at the end of the day
    private fun getLikeKeysForComment(commentId: Long): List<Key<Like>> {
        return ofy()
            .load()
            .type(Like::class.java)
            .filter(Like.INDEX_LIKEABLE_ID, commentId)
            .keys()
            .list()
    }

    data class Input(val requesterId: Long, val postId: String, val commentId: String)
}
