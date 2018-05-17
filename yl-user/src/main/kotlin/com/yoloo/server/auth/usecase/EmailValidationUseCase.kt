package com.yoloo.server.auth.usecase

import com.google.appengine.api.memcache.MemcacheService
import org.springframework.stereotype.Component

@Component
class EmailValidationUseCase(private val memcacheService: MemcacheService) {

    fun execute(email: String) {

    }
}