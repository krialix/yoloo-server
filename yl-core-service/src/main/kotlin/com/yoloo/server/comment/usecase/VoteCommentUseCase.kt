package com.yoloo.server.comment.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.appengine.util.AppengineEnv
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.common.exception.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter

class VoteCommentUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")

        val vote = Vote.create(requesterId, commentId, "c")

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.insert(vote.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        comment.voteCount = comment.voteCount.inc()

        val saveFuture = ofy().save().entities(comment, vote)

        if (AppengineEnv.isTest()) {
            putFuture.get()
            saveFuture.now()
        }
    }
}