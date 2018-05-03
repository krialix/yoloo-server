package com.yoloo.server.user.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.user.domain.response.EmailValidResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class CheckEmailValidationUseCase(private val memcacheService: MemcacheService) : UseCase<String, EmailValidResponse> {

    override fun execute(email: String): EmailValidResponse {
        val filter = memcacheService.get(Filters.KEY_FILTER_USERS) as NanoCuckooFilter

        return EmailValidResponse(!filter.contains(email))
    }
}