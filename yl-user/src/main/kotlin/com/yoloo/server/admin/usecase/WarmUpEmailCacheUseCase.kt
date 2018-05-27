package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.auth.entity.Account
import com.yoloo.server.objectify.ObjectifyProxy.ofy
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
            .type(Account::class.java)
            .iterable()
            .asSequence()
            .map { it.email.value }
            .toList()
            .let {
                LOGGER.info("Created cuckoo filter for emails")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it) }

                memcacheService.put(Account.KEY_FILTER_EMAIL, cuckooFilter)
            }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WarmUpEmailCacheUseCase::class.java)
    }
}