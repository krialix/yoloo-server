package com.yoloo.server.admin.api

import com.yoloo.server.admin.usecase.WarmUpRelationshipCacheUseCase
import com.yoloo.server.admin.usecase.WarmUpUserIdentifierCacheUseCase
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/_ah/admin",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AdminController(
    private val warmUpUserIdentifierCacheUseCase: WarmUpUserIdentifierCacheUseCase,
    private val warmUpRelationshipCacheUseCase: WarmUpRelationshipCacheUseCase
) {

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cache/emails/warmup")
    fun warmUpEmailCache() {
        warmUpUserIdentifierCacheUseCase.execute()
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cache/relationships/warmup")
    fun warmUpRelationshipCache() {
        warmUpRelationshipCacheUseCase.execute()
    }
}
