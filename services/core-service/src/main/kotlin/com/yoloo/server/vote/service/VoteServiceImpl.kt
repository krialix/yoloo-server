package com.yoloo.server.vote.service

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.comment.util.CommentErrors
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.entity.Votable
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.vote.entity.Vote
import com.yoloo.server.util.VoteErrors
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class VoteServiceImpl(
    private val memcacheService: AsyncMemcacheService
) : VoteService {

    override fun vote(userId: Long, votableId: Long, type: Class<out Votable>) {
        val entityFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter

        ServiceExceptions.checkNotFound(entityFilter.contains(userId), PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkNotFound(entityFilter.contains(votableId), PostErrors.ERROR_POST_NOT_FOUND)

        val votable = ofy().load().key(Key.create(type, votableId)).now()

        ServiceExceptions.checkForbidden(votable.isVotingAllowed(), VoteErrors.ERROR_VOTE_FORBIDDEN)

        val vote = Vote.create(userId, votableId)

        ServiceExceptions.checkConflict(!entityFilter.contains(vote.id), VoteErrors.ERROR_VOTE_CONFLICT)

        entityFilter.insert(vote.id)

        memcacheService.put(Vote.KEY_FILTER_VOTE, entityFilter)

        ofy().save().entities(votable, vote)
    }

    override fun unvote(userId: Long, votableId: Long, type: Class<out Votable>) {
        val votableKey = Key.create(type, votableId)
        val voteKey = Vote.createKey(userId, votableId)

        val map = ofy().load().keys(votableKey, voteKey) as Map<*, *>
        val votable = map[votableKey] as Votable?
        val vote = map[voteKey] as Vote?

        ServiceExceptions.checkNotFound(votable != null, CommentErrors.ERROR_COMMENT_NOT_FOUND)
        ServiceExceptions.checkNotFound(vote != null, VoteErrors.ERROR_VOTE_NOT_FOUND)

        val voteFilter = getEntityFilter()
        voteFilter.delete(vote!!.id)
        memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        votable!!.unvote()

        ofy().save().entity(votable)
        ofy().delete().entity(vote)
    }

    private fun getEntityFilter() = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
}
