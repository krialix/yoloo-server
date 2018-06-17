package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter

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
                val filter = NanoCuckooFilter.Builder(32).build()
                it.forEach { filter.insert(it) }

                memcacheService.put(User.KEY_FILTER_USER_IDENTIFIER, filter)
            }
    }
}