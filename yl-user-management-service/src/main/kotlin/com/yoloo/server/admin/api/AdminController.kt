package com.yoloo.server.admin.api

import com.yoloo.server.admin.usecase.DeleteUserUseCase
import com.yoloo.server.admin.usecase.WarmUpRelationshipCacheUseCase
import com.yoloo.server.admin.usecase.WarmUpUserIdentifierCacheUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    "/api/admin",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AdminController(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val warmUpUserIdentifierCacheUseCase: WarmUpUserIdentifierCacheUseCase,
    private val warmUpRelationshipCacheUseCase: WarmUpRelationshipCacheUseCase
) {

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/delete/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("userId") userId: String) {
        deleteUserUseCase.execute(userId)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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