package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.rest.exception.ServiceExceptions
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
        if (AppengineUtil.isTest()) {
            putFuture.get()
        }

        post!!.countData.voteCount = post.countData.voteCount.dec()
        val saveFuture = ofy().save().entities(post)
        if (AppengineUtil.isTest()) {
            saveFuture.now()
        }

        val deleteFuture = ofy().delete().entity(bookmark)
        if (AppengineUtil.isTest()) {
            deleteFuture.now()
        }
    }
}