package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class VoteCommentUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, commentId: Long) {
        val comment = ofy().load().type(Comment::class.java).id(commentId).now()

        ServiceExceptions.checkNotFound(comment != null, "comment.not_found")

        val vote = Vote(Vote.createId(requesterId, commentId, "c"), 1)

        val voteFilter = memcacheService.get(Filters.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        voteFilter.insert(vote.id)
        val putFuture = memcacheService.put(Filters.KEY_FILTER_VOTE, voteFilter)
        if (AppengineUtil.isTest()) {
            putFuture.get()
        }

        comment.voteCount = comment.voteCount.inc()
        val result = ofy().save().entities(comment, vote)
        if (AppengineUtil.isTest()) {
            result.now()
        }
    }
}