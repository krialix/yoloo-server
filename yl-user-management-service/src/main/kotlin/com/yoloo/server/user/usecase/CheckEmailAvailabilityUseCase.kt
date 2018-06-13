package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.rest.exception.ServiceExceptions
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.CheckEmailAvailabilityRequest
import net.cinnom.nanocuckoo.NanoCuckooFilter

class CheckEmailAvailabilityUseCase(private val memcacheService: MemcacheService) {

    fun execute(request: CheckEmailAvailabilityRequest) {
        val emailFilter = memcacheService.get(User.KEY_FILTER_USER_IDENTIFIER) as NanoCuckooFilter

        ServiceExceptions.checkConflict(!emailFilter.contains(request.email), "user.email.conflict")
    }
}