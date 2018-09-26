package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.util.BookmarkErrors
import com.yoloo.server.post.util.PostErrors
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class UnbookmarkPostUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, postId: Long) {
        val postKey = Post.createKey(postId)
        val bookmarkKey = Bookmark.createKey(requesterId, postId)

        val map = ofy().load().keys(postKey, bookmarkKey) as Map<*, *>
        val post = map[postKey] as Post?
        val bookmark = map[bookmarkKey] as Bookmark?

        ServiceExceptions.checkNotFound(post != null, PostErrors.NOT_FOUND)
        ServiceExceptions.checkNotFound(bookmark != null, BookmarkErrors.ERROR_BOOKMARK_NOT_FOUND)

        updateMemcache(bookmarkKey)

        ofy().save().entity(post)
        ofy().delete().entity(bookmark)
    }

    fun updateMemcache(bookmarkKey: Key<Bookmark>) {
        val bookmarkFilter =
            memcacheService.get(Bookmark.KEY_FILTER_BOOKMARK).get() as NanoCuckooFilter
        bookmarkFilter.delete(bookmarkKey.name)
        memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, bookmarkFilter)
    }
}
