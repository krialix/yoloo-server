package com.yoloo.server.admin.api

import com.yoloo.server.admin.usecase.DeleteUserUseCase
import com.yoloo.server.admin.usecase.WarmUpRelationshipCacheUseCase
import com.yoloo.server.admin.usecase.WarmUpEmailCacheUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/admin",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AdminControllerV1(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val warmUpEmailCacheUseCase: WarmUpEmailCacheUseCase,
    private val warmUpRelationshipCacheUseCase: WarmUpRelationshipCacheUseCase
) {

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(principal: Principal, @PathVariable("userId") userId: String) {
        deleteUserUseCase.execute(principal, userId)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cache/emails/warmup")
    fun warmUpEmailCache() {
        warmUpEmailCacheUseCase.execute()
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cache/relationships/warmup")
    fun warmUpRelationshipCache() {
        warmUpRelationshipCacheUseCase.execute()
    }
}