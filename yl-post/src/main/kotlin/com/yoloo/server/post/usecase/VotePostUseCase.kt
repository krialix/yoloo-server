package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.vo.PostPermFlag
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class VotePostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "postId is invalid")
        val votingDisabled = post.flags.contains(PostPermFlag.DISABLE_VOTING)
        ServiceExceptions.checkForbidden(!votingDisabled, "voting for the post is not allowed")

        val vote = Vote(Vote.createId(requesterId, postId, "p"), 1)

        val voteFilter = memcacheService.get(Filters.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.insert(vote.id)
        val putFuture = memcacheService.put(Filters.KEY_FILTER_VOTE, voteFilter)
        if (AppengineUtil.isTest()) {
            putFuture.get()
        }

        post.countData.voteCount = post.countData.voteCount.inc()
        val result = ofy().save().entities(post, vote)
        if (AppengineUtil.isTest()) {
            result.now()
        }
    }
}