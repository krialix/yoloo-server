package com.yoloo.server.user.api

import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.user.usecase.GetUserUseCase
import com.yoloo.server.user.usecase.PatchUserUseCase
import com.yoloo.server.user.usecase.SearchUserUseCase
import com.yoloo.server.user.vo.PatchUserRequest
import com.yoloo.server.user.vo.SearchUserResponse
import com.yoloo.server.user.vo.UserResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping(
    "/api/v1/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class UserControllerV1(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase
) {
    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:read')")
    @GetMapping("/{userId}")
    fun getUser(authentication: Authentication, @PathVariable("userId") userId: Long): UserResponse {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        println("Auth: $authentication")
        println("Details: $details")

        return getUserUseCase.execute(GetUserUseCase.Params(jwtClaim, userId))
    }

    @PatchMapping
    fun patchUser(authentication: Authentication, @RequestBody @Valid request: PatchUserRequest) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        patchUserUseCase.execute(PatchUserUseCase.Params(jwtClaim, request))
    }

    @GetMapping("/search", params = ["q"])
    fun searchUsers(
        principal: Principal?,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(SearchUserUseCase.Params(query, cursor))
    }
}