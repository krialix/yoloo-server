package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.google.common.collect.ImmutableList
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkForbidden
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.entity.service.EntityCacheService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.util.CommentErrors
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import com.yoloo.spring.autoconfiguration.appengine.services.counter.CounterService
import org.springframework.stereotype.Service

@Service
class DeleteCommentUseCase(
    private val entityCacheService: EntityCacheService,
    private val counterService: CounterService,
    private val hashids: Hashids
) : AbstractUseCase<DeleteCommentUseCase.Input, UseCase.Output.Void>() {

    override fun onExecute(input: Input): UseCase.Output.Void {
        val commentHashId = hashids.decode(input.commentId)

        val commentId = commentHashId[0]
        val commentAuthorId = commentHashId[1]

        val entityCache = entityCacheService.get()

        checkNotFound(entityCache.contains(commentId), CommentErrors.NOT_FOUND)
        checkForbidden(commentAuthorId == input.requesterId, CommentErrors.FORBIDDEN)

        val commentKey = Comment.createKey(commentId)

        val comment = ofy().load().key(commentKey).now()

        checkForbidden(!comment.approved, CommentErrors.FORBIDDEN_APPROVED)

        val likeKeys = getLikeKeysForComment(commentId)

        val pendingDeleteKeys = ImmutableList.builder<Key<*>>()
            .add(comment.key)
            .addAll(likeKeys)
            .build()

        ofy().defer().delete().keys(pendingDeleteKeys)

        counterService.decrement("POST_COMMENT:${comment.postId.value}", "USER_COMMENT:${comment.author.id}")

        return UseCase.Output.Void.getInstance()
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

    data class Input(val requesterId: Long, val postId: String, val commentId: String) : UseCase.Input
}
