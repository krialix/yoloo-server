package com.yoloo.server.bookmark.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.bookmark.entity.Bookmark
import com.googlecode.objectify.ObjectifyService.ofy
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class RefreshBookmarkCacheUseCase(private val memcacheService: AsyncMemcacheService) {

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
                val cuckooFilter = NanoCuckooFilter.Builder(BOOKMARK_FILTER_CAPACITY).build()
                it.forEach { cuckooFilter.insert(it) }

                memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, cuckooFilter)
            }
    }

    companion object {
        private val LOGGER: Logger = LogManager.getLogger()

        private const val BOOKMARK_FILTER_CAPACITY = 1_000_000L
    }
}
