package com.yoloo.server.bookmark.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.bookmark.entity.Bookmark
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Post
import net.cinnom.nanocuckoo.NanoCuckooFilter

class BookmarkPostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Post.createKey(postId)
        val bookmarkKey = Bookmark.createKey(requesterId, postId)

        val map = ofy().load().keys(postKey, bookmarkKey) as Map<*, *>
        val post = map[postKey] as Post?
        val bookmark = map[bookmarkKey] as Bookmark?

        ServiceExceptions.checkNotFound(post != null, Post.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkConflict(bookmark == null, Bookmark.ERROR_BOOKMARK_CONFLICT)

        val bookmarkFilter =
            memcacheService.get(Bookmark.KEY_FILTER_BOOKMARK).get() as NanoCuckooFilter
        bookmarkFilter.insert(bookmarkKey.name)
        memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, bookmarkFilter)

        val newBookmark = Bookmark.create(requesterId, postId)

        ofy().save().entities(post, newBookmark)
    }
}
