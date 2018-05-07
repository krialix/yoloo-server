package com.yoloo.server.admin.application

import com.yoloo.server.admin.domain.usecase.DeleteUserUseCase
import com.yoloo.server.admin.domain.usecase.WarmupCacheUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/admin",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AdminControllerV1 @Autowired constructor(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val warmupCacheUseCase: WarmupCacheUseCase
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
        warmupCacheUseCase.execute(Unit)
    }
}