package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.Relationship
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class WarmUpRelationshipCacheUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute() {
        LOGGER.info("Relationship cache warming up")

        return ofy()
            .load()
            .type(Relationship::class.java)
            .keys()
            .iterable()
            .let {
                LOGGER.info("Created cuckoo filter for relationships")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it.name) }

                memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, cuckooFilter)
            }
    }

    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(WarmUpRelationshipCacheUseCase::class.java)
    }
}