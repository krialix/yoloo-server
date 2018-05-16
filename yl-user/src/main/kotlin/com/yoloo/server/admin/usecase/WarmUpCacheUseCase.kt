package com.yoloo.server.admin.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.auth.entity.Account
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.Relationship
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WarmUpCacheUseCase(private val memcacheService: AsyncMemcacheService) : UseCase<Unit, Unit> {

    override fun execute(request: Unit) {
        val emailCache = warmupEmailCache()
        val relationshipCache = warmupRelationshipCache()

        memcacheService.putAll(mapOf(emailCache, relationshipCache))
    }

    private fun warmupEmailCache(): Pair<String, NanoCuckooFilter> {
        log.info("Email cache warmed")

        return ofy()
            .load()
            .type(Account::class.java)
            //.project("account.email.value")
            .iterable()
            .asSequence()
            .map { it.email.value }
            .toList()
            .let {
                log.info("Creating new cuckoo filter for email")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert(it) }

                return@let Pair(Filters.KEY_FILTER_EMAIL, cuckooFilter)
            }
    }

    private fun warmupRelationshipCache(): Pair<String, NanoCuckooFilter> {
        log.info("Relationship cache warmed")

        return ofy()
            .load()
            .type(Relationship::class.java)
            .iterable()
            .let {
                log.info("Creating new cuckoo filter for relationship")
                val cuckooFilter = NanoCuckooFilter.Builder(32).build()
                it.forEach { cuckooFilter.insert("${it.fromId}:${it.toId}") }

                return@let Pair(Filters.KEY_FILTER_RELATIONSHIP, cuckooFilter)
            }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(WarmUpCacheUseCase::class.java)
    }
}