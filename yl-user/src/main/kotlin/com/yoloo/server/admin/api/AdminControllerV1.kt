package com.yoloo.server.admin.api

import com.yoloo.server.admin.usecase.DeleteUserUseCase
import com.yoloo.server.admin.usecase.WarmUpCacheUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/admin",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AdminControllerV1(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val warmUpCacheUseCase: WarmUpCacheUseCase
) {
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun deleteUser(principal: Principal?, @PathVariable("userId") userId: String) {
        deleteUserUseCase.execute(DeleteUserUseCase.Request(principal, userId))
    }

    @PostMapping("cache/warmup")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun warmupCache() {
        warmUpCacheUseCase.execute(Unit)
    }
}