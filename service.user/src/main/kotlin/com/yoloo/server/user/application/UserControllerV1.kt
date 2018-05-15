package com.yoloo.server.user.application

import com.yoloo.server.auth.domain.vo.JwtClaims
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.user.domain.requestpayload.PatchUserPayload
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.PatchUserUseCase
import com.yoloo.server.user.domain.usecase.SearchUserUseCase
import org.springframework.beans.factory.annotation.Autowired
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
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class UserControllerV1 @Autowired constructor(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase
) {
    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/{userId}")
    fun getUser(authentication: Authentication?, @PathVariable("userId") userId: Long): UserResponse {
        authentication?.let {
            println(it)
        }
        val details = authentication!!.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return getUserUseCase.execute(GetUserUseCase.Params(jwtClaim, userId))
    }

    @PatchMapping
    fun patchUser(
        principal: Principal?,
        @RequestBody @Valid payload: PatchUserPayload
    ) {
        patchUserUseCase.execute(PatchUserUseCase.Request(principal, payload))
    }

    @GetMapping("/search", params = ["q"])
    fun searchUsers(
        principal: Principal?,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(SearchUserUseCase.Request(query, cursor))
    }
}