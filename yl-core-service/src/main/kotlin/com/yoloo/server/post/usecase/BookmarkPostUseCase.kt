package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.appengine.util.AppengineEnv
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.common.exception.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter

class BookmarkPostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Key.create(Post::class.java, postId)
        val bookmarkKey = Bookmark.createKey(requesterId, postId)

        val map = ofy().load().keys(postKey, bookmarkKey) as Map<*, *>
        val post = map[postKey] as Post?
        val bookmark = map[bookmarkKey] as Bookmark?

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkConflict(bookmark == null, "bookmark.conflict")

        val bookmarkFilter =
            memcacheService.get(Bookmark.KEY_FILTER_BOOKMARK).get() as NanoCuckooFilter
        bookmarkFilter.insert(bookmarkKey.name)
        val putFuture = memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, bookmarkFilter)

        val newBookmark = Bookmark.create(requesterId, postId)

        val saveFuture = ofy().save().entities(post, newBookmark)

        if (AppengineEnv.isTest()) {
            saveFuture.now()
            putFuture.get()
        }
    }
}