package com.yoloo.server.vote.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.vote.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter

class UnvotePostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val voteKey = Vote.createKey(requesterId, postId, "p")

        val map = ofy().load().keys(postKey, voteKey) as Map<*, *>
        val post = map[postKey] as Post?
        val vote = map[voteKey] as Vote?

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(vote != null, "vote.not_found")

        val deleteResult = ofy().delete().key(voteKey)

        val voteFilter = getVoteFilter()
        voteFilter.delete(vote!!.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        post!!.countData.voteCount = post.countData.voteCount.dec()

        val saveResult = ofy().save().entity(post)

        TestUtil.saveNow(saveResult, deleteResult)
        TestUtil.saveNow(putFuture)
    }

    private fun getVoteFilter(): NanoCuckooFilter {
        return memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
    }
}
