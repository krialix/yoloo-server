package com.yoloo.server.vote.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.vote.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter

class VoteCommentUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")

        val voteFilter = getVoteFilter()
        val voted = Vote.isVoted(voteFilter, requesterId, commentId, "c")

        ServiceExceptions.checkConflict(!voted, "vote.conflict")

        val vote = Vote.create(requesterId, commentId, "c")

        voteFilter.insert(vote.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        comment.voteCount = comment.voteCount.inc()

        val saveResult = ofy().save().entities(comment, vote)

        TestUtil.saveNow(putFuture)
        TestUtil.saveNow(saveResult)
    }

    private fun getVoteFilter(): NanoCuckooFilter {
        return memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
    }
}