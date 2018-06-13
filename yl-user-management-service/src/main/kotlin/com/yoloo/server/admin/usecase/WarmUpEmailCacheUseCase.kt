package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class WarmUpEmailCacheUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute() {
        LOGGER.info("Email cache warming up")

        return ofy()
            .load()
            .type(User::class.java)
            .iterable()
            .asSequence()
            .map { it.email.value }
            .toList()
            .let {
                LOGGER.info("Created cuckoo filter for emails")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it) }

                memcacheService.put(User.KEY_FILTER_USER_IDENTIFIER, cuckooFilter)
            }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WarmUpEmailCacheUseCase::class.java)
    }
}