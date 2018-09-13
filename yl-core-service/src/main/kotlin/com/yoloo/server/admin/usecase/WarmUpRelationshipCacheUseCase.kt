package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.relationship.entity.Relationship
import net.cinnom.nanocuckoo.NanoCuckooFilter

class WarmUpRelationshipCacheUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute() {
        return ofy()
            .load()
            .type(Relationship::class.java)
            .keys()
            .iterable()
            .let { it ->
                val filter = NanoCuckooFilter.Builder(RELATIONSHIP_FILTER_CAPACITY).build()
                it.forEach { filter.insert(it.name) }

                memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, filter)
            }
    }

    companion object {
        private const val RELATIONSHIP_FILTER_CAPACITY = 1_000_000L
    }
}
