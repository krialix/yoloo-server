package com.yoloo.server.vote.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.PostPermFlag
import com.yoloo.server.vote.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter

class VotePostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        val votingDisabled = post.flags.contains(PostPermFlag.DISABLE_VOTING)
        ServiceExceptions.checkForbidden(!votingDisabled, "post.forbidden_voting")

        val voteFilter = getVoteFilter()
        val voted = Vote.isVoted(voteFilter, requesterId, postId, "p")

        ServiceExceptions.checkConflict(!voted, "vote.conflict")

        val vote = Vote.create(requesterId, postId, "p")

        voteFilter.insert(vote.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        post.countData.voteCount = post.countData.voteCount.inc()

        val saveResult = ofy().save().entities(post, vote)

        TestUtil.saveNow(saveResult)
        TestUtil.saveNow(putFuture)
    }

    private fun getVoteFilter(): NanoCuckooFilter {
        return memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
    }
}
