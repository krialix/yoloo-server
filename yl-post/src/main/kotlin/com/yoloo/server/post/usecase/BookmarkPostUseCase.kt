package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.api.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class BookmarkPostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val bookmarkKey = Bookmark.createKey(requesterId, postId)

        val map = ofy().load().keys(postKey, bookmarkKey) as Map<*, *>
        val post = map[postKey] as Post?
        val bookmark = map[bookmarkKey] as Bookmark?

        com.yoloo.server.api.exception.ServiceExceptions.checkNotFound(post != null, "post.not_found")
        com.yoloo.server.api.exception.ServiceExceptions.checkConflict(bookmark == null, "bookmark.conflict")

        val bookmarkFilter = memcacheService.get(Bookmark.KEY_FILTER_BOOKMARK).get() as NanoCuckooFilter
        bookmarkFilter.insert(bookmarkKey.name)
        val putFuture = memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, bookmarkFilter)
        if (AppengineUtil.isTest()) {
            putFuture.get()
        }

        post!!.countData.voteCount = post.countData.voteCount.inc()
        val result = ofy().save().entities(post, bookmark)
        if (AppengineUtil.isTest()) {
            result.now()
        }
    }
}