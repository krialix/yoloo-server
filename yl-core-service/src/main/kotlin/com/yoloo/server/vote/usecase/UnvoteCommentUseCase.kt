package com.yoloo.server.vote.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.vote.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter

class UnvoteCommentUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, commentId: Long) {
        val commentKey = Key.create(Comment::class.java, commentId)
        val voteKey = Vote.createKey(requesterId, commentId, "c")

        val map = ofy().load().keys(commentKey, voteKey) as Map<*, *>
        val comment = map[commentKey] as Comment?
        val vote = map[voteKey] as Vote?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkNotFound(vote != null, "vote.not_found")

        val deleteFuture = ofy().delete().key(voteKey)

        val voteFilter = getVoteFilter()
        voteFilter.delete(vote!!.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        comment!!.voteCount = comment.voteCount.dec()

        val saveResult = ofy().save().entity(comment)

        TestUtil.saveNow(saveResult, deleteFuture)
        TestUtil.saveNow(putFuture)
    }

    private fun getVoteFilter(): NanoCuckooFilter {
        return memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
    }
}
