package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService

class EmailValidationUseCase(private val memcacheService: MemcacheService) {

    fun execute(email: String) {

    }
}