package com.yoloo.server.user.infrastructure.eventlistener

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.util.Filters
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class StartupApplicationListener(
    private val memcacheService: MemcacheService
) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("APPLICATION PROPERTIES INITIALIZING...")

        val map = mapOf<Any, Any>(
            Filters.KEY_FILTER_USER_EMAIL to NanoCuckooFilter.Builder(32).build(),
            "f_subscription:100" to NanoCuckooFilter.Builder(32).build()
        )

        memcacheService.putAll(map)

        logger.info("APPLICATION PROPERTIES INITIALIZED")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(StartupApplicationListener::class.java)
    }
}