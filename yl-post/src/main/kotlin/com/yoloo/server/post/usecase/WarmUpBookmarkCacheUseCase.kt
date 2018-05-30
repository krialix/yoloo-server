package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

class WarmUpBookmarkCacheUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute() {
        LOGGER.info("Bookmark cache warming up")

        ofy()
            .load()
            .type(Bookmark::class.java)
            .keys()
            .iterable()
            .asSequence()
            .map { it.kind }
            .toList()
            .let {
                LOGGER.info("Created cuckoo filter for bookmarks")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it) }

                memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, cuckooFilter)
            }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WarmUpBookmarkCacheUseCase::class.java)
    }
}