package com.yoloo.server.vote.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.vote.vo.Votable
import com.yoloo.server.vote.entity.Vote
import com.yoloo.server.vote.util.VoteErrors
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class VoteUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, votableId: Long, type: Class<out Votable>) {
        val votable = ofy().load().key(Key.create(type, votableId)).now()

        ServiceExceptions.checkNotFound(votable != null, PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkForbidden(votable.isVotingAllowed(), VoteErrors.ERROR_VOTE_FORBIDDEN)

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        val voted = isVoted(voteFilter, requesterId, votableId)

        ServiceExceptions.checkConflict(!voted, VoteErrors.ERROR_VOTE_CONFLICT)

        val vote = Vote.create(requesterId, votableId)

        voteFilter.insert(vote.id)
        memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        votable.vote()

        ofy().save().entities(votable, vote)
    }

    private fun isVoted(filter: NanoCuckooFilter, requesterId: Long, votableId: Long): Boolean {
        return filter.contains(Vote.createId(requesterId, votableId))
    }
}
