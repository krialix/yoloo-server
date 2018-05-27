package com.yoloo.server.auth.usecase

import com.google.appengine.api.memcache.MemcacheService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class EmailValidationUseCase(private val memcacheService: MemcacheService) {

    fun execute(email: String) {

    }
}