package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.appengine.util.AppengineEnv
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.vo.PostPermFlag
import com.yoloo.server.common.exception.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter

class VotePostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        val votingDisabled = post.flags.contains(PostPermFlag.DISABLE_VOTING)
        ServiceExceptions.checkForbidden(!votingDisabled, "post.forbidden_voting")

        val vote = Vote.create(requesterId, postId, "p")

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.insert(vote.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        post.countData.voteCount = post.countData.voteCount.inc()

        val saveFuture = ofy().save().entities(post, vote)

        if (AppengineEnv.isTest()) {
            putFuture.get()
            saveFuture.now()
        }
    }
}