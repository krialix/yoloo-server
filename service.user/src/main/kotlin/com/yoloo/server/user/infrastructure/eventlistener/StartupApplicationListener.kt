package com.yoloo.server.user.infrastructure.eventlistener

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.util.Filters
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

///@Profile("dev")
//@Component
class StartupApplicationListener(
    private val memcacheService: MemcacheService
) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("--- StartupApplicationListener INITIALIZING ---")

        ofy()
            .load()
            .type(User::class.java)
            .project("account.email.value")
            .list()
            .asSequence()
            .map { it.account.email.value }
            .toList()
            .let {
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it) }
                memcacheService.put(Filters.KEY_FILTER_EMAIL, cuckooFilter)
            }

        logger.info("--- StartupApplicationListener INITIALIZED ---")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(StartupApplicationListener::class.java)
    }
}