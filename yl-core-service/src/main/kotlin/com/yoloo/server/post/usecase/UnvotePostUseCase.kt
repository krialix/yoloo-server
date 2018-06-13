package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.appengine.util.AppengineEnv
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.common.exception.exception.ServiceExceptions
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

        val deleteFuture = ofy().delete().key(voteKey)

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.delete(vote!!.id)
        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)

        post!!.countData.voteCount = post.countData.voteCount.dec()

        val saveFuture = ofy().save().entity(post)

        if (AppengineEnv.isTest()) {
            saveFuture.now()
            putFuture.get()
            deleteFuture.now()
        }
    }
}