package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.rest.error.exception.ServiceExceptions
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class UnvoteCommentUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, commentId: Long) {
        val commentKey = Key.create(Comment::class.java, commentId)
        val voteKey = Vote.createKey(requesterId, commentId, "c")

        val map = ofy().load().keys(commentKey, voteKey) as Map<*, *>
        val comment = map[commentKey] as Comment?
        val vote = map[voteKey] as Vote?

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")
        ServiceExceptions.checkNotFound(vote != null, "vote.not_found")

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

        comment!!.voteCount = comment.voteCount.dec()
        val saveResult = ofy().save().entity(comment)
        if (AppengineUtil.isTest()) {
            saveResult.now()
        }
    }
}