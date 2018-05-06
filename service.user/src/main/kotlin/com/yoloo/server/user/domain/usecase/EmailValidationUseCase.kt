package com.yoloo.server.user.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkConflict
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class EmailValidationUseCase(private val memcacheService: MemcacheService) : UseCase<String, Unit> {

    override fun execute(email: String) {
        checkEmailIsUnique(email)
    }

    private fun checkEmailIsUnique(email: String) {
        val emailFilter = memcacheService.get(Filters.KEY_FILTER_EMAIL) as NanoCuckooFilter? ?: ofy()
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
                return@let cuckooFilter
            }

        memcacheService.put(Filters.KEY_FILTER_EMAIL, emailFilter)

        checkConflict(!emailFilter.contains(email), "user.error.exists")
    }
}