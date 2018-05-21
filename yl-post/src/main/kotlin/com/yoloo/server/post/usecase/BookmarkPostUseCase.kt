package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class BookmarkPostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val bookmarkBucketKey = Bookmark.createKey(requesterId)

        val map = ofy().load().keys(postKey, bookmarkBucketKey) as Map<*, *>
        val post = map[postKey] as Post?
        val bookmarkBucket =
            map[bookmarkBucketKey] as Bookmark? ?: Bookmark(Bookmark.createId(requesterId))

        ServiceExceptions.checkNotFound(post != null, "post.not_found")

        val bookmarkFilter = memcacheService.get(Filters.KEY_FILTER_BOOKMARK).get() as NanoCuckooFilter
        bookmarkFilter.insert(vote.id)
        val putFuture = memcacheService.put(Filters.KEY_FILTER_VOTE, bookmarkFilter)
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