package com.yoloo.server.vote.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.comment.util.CommentErrors
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.vote.entity.Votable
import com.yoloo.server.vote.entity.Vote
import com.yoloo.server.vote.util.VoteErrors
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class UnvoteUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, votableId: Long, type: Class<out Votable>) {
        val votableKey = Key.create(type, votableId)
        val voteKey = Vote.createKey(requesterId, votableId)

        val map = ofy().load().keys(votableKey, voteKey) as Map<*, *>
        val votable = map[votableKey] as Votable?
        val vote = map[voteKey] as Vote?

        ServiceExceptions.checkNotFound(votable != null, CommentErrors.ERROR_COMMENT_NOT_FOUND)
        ServiceExceptions.checkNotFound(vote != null, VoteErrors.ERROR_VOTE_NOT_FOUND)

        updateMemcache(vote)

        votable!!.unvote()

        ofy().save().entity(votable)
        ofy().delete().entity(vote)
    }

    private fun updateMemcache(vote: Vote?) {
        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.delete(vote!!.id)
        memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)
    }
}
