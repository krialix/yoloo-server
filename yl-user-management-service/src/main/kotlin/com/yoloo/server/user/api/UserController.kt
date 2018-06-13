package com.yoloo.server.user.api

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.user.usecase.*
import com.yoloo.server.user.vo.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(
    "/api/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase,
    private val checkEmailAvailabilityUseCase: CheckEmailAvailabilityUseCase,
    private val userRegisterUseCase: UserRegisterUseCase
) {

    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid @NotBlank request: CheckEmailAvailabilityRequest?) {
        checkEmailAvailabilityUseCase.execute(request!!)
    }

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid @NotNull request: UserRegisterRequest?): String {
        return userRegisterUseCase.execute(request!!)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/{userId}")
    fun getUser(authentication: Authentication, @PathVariable("userId") userId: Long): UserResponse {
        println("Details - ${(authentication.details as OAuth2AuthenticationDetails).decodedDetails}")
        println("Principal - ${authentication.principal}")
        println("Credentials - ${authentication.credentials}")
        val jwtClaim = JwtClaims.from(authentication)

        return getUserUseCase.execute(jwtClaim.sub, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PatchMapping
    fun patchUser(authentication: Authentication, @RequestBody @Valid @NotNull request: PatchUserRequest?) {
        val jwtClaim = JwtClaims.from(authentication)

        patchUserUseCase.execute(jwtClaim.sub, request!!)
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