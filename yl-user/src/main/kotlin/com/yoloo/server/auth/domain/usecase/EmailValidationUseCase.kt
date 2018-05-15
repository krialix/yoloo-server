package com.yoloo.server.auth.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.shared.UseCase
import org.springframework.stereotype.Component

@Component
class EmailValidationUseCase(private val memcacheService: MemcacheService) : UseCase<String, Unit> {

    override fun execute(email: String) {

    }
}