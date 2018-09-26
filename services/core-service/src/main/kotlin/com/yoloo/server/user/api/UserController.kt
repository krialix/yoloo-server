package com.yoloo.server.user.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.user.usecase.*
import com.yoloo.server.user.vo.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(
    "/api/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val checkEmailAvailabilityUseCase: CheckEmailAvailabilityUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) {

    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid request: CheckEmailAvailabilityRequest?) {
        ServiceExceptions.checkBadRequest(request != null, "request.body.empty")

        checkEmailAvailabilityUseCase.execute(request!!)
    }

    @PreAuthorize("hasAnyRole('ANONYMOUS')")
    @PostMapping("/signUp")
    fun register(@RequestBody @Valid request: UserCreateRequest?): ResponseEntity<UserResponse> {
        ServiceExceptions.checkBadRequest(request != null, "request.body.empty")

        return createUserUseCase.execute(request!!)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/{userId}")
    fun getUser(authentication: Authentication, @PathVariable("userId") userId: String): UserResponse {
        val user = AuthUtil.from(authentication)

        return getUserUseCase.execute(GetUserUseCase.Input(user.userId.toString(), userId))
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PatchMapping
    fun patchUser(authentication: Authentication, @RequestBody @Valid request: PatchUserRequest?) {
        val user = AuthUtil.from(authentication)

        updateUserUseCase.execute(user.userId, request!!)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("userId") userId: String) {
        deleteUserUseCase.execute(userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/search", params = ["q"])
    fun searchUsers(
        authentication: Authentication,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(query, cursor)
    }
}
