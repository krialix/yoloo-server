package com.yoloo.server.admin.application

import com.yoloo.server.common.util.RestMediaType
import com.yoloo.server.admin.domain.usecase.DeleteUserUseCase
import com.yoloo.server.admin.domain.usecase.contract.DeleteUserUseCaseContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/admin",
    produces = [RestMediaType.APPLICATION_API_JSON_VND_VALUE],
    consumes = [RestMediaType.APPLICATION_API_JSON_VND_VALUE]
)
class AdminControllerV1 @Autowired constructor(
    private val deleteUserUseCase: DeleteUserUseCase
) {

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun deleteUser(principal: Principal, @PathVariable("userId") userId: String) {
        deleteUserUseCase.execute(DeleteUserUseCaseContract.Request(principal, userId))
    }
}