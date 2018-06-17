package com.yoloo.server.bookmark.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.bookmark.entity.Bookmark
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import net.cinnom.nanocuckoo.NanoCuckooFilter

class UnbookmarkPostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val bookmarkKey = Bookmark.createKey(requesterId, postId)

        val map = ofy().load().keys(postKey, bookmarkKey) as Map<*, *>
        val post = map[postKey] as Post?
        val bookmark = map[bookmarkKey] as Bookmark?

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(bookmark != null, "bookmark.not_found")

        val bookmarkFilter =
            memcacheService.get(Bookmark.KEY_FILTER_BOOKMARK).get() as NanoCuckooFilter
        bookmarkFilter.delete(bookmarkKey.name)
        val putFuture = memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, bookmarkFilter)

        val saveResult = ofy().save().entity(post)
        val deleteResult = ofy().delete().entity(bookmark)

        TestUtil.saveResultsNowIfTest(saveResult, deleteResult)
        TestUtil.saveFuturesNowIfTest(putFuture)
    }
}