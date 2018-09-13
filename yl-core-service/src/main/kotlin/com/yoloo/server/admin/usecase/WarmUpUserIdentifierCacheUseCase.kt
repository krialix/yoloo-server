package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class WarmUpUserIdentifierCacheUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute() {
        return ofy()
            .load()
            .type(User::class.java)
            .iterable()
            .asSequence()
            .map { it.email.value }
            .toList()
            .let {
                val filter = NanoCuckooFilter.Builder(USER_IDENTIFIER_FILTER_CAPACITY).build()
                it.forEach { filter.insert(it) }

                memcacheService.put(User.KEY_FILTER_USER_IDENTIFIER, filter)
            }
    }

    companion object {
        private const val USER_IDENTIFIER_FILTER_CAPACITY = 1_000_000L
    }
}
