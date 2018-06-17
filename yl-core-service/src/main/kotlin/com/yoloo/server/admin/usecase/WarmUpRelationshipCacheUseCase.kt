package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.entity.Relationship
import net.cinnom.nanocuckoo.NanoCuckooFilter

class WarmUpRelationshipCacheUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute() {
        return ofy()
            .load()
            .type(Relationship::class.java)
            .keys()
            .iterable()
            .let {
                val filter = NanoCuckooFilter.Builder(32).build()
                it.forEach { filter.insert(it.name) }

                memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, filter)
            }
    }
}