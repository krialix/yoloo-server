package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.api.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class UnvotePostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val voteKey = Vote.createKey(requesterId, postId, "p")

        val map = ofy().load().keys(postKey, voteKey) as Map<*, *>
        val post = map[postKey] as Post?
        val vote = map[voteKey] as Vote?

        com.yoloo.server.api.exception.ServiceExceptions.checkNotFound(post != null, "post.not_found")
        com.yoloo.server.api.exception.ServiceExceptions.checkNotFound(vote != null, "vote.not_found")

        val deleteResult = ofy().delete().key(voteKey)
        if (AppengineUtil.isTest()) {
            deleteResult.now()
        }

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.delete(vote!!.id)

        val putFuture = memcacheService.put(Vote.KEY_FILTER_VOTE, voteFilter)
        if (AppengineUtil.isTest()) {
            putFuture.get()
        }

        post!!.countData.voteCount = post.countData.voteCount.dec()
        val saveResult = ofy().save().entity(post)
        if (AppengineUtil.isTest()) {
            saveResult.now()
        }
    }
}