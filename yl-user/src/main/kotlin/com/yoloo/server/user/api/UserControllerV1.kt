package com.yoloo.server.user.api

import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.user.usecase.*
import com.yoloo.server.user.vo.PatchUserRequest
import com.yoloo.server.user.vo.RelationshipResponse
import com.yoloo.server.user.vo.SearchUserResponse
import com.yoloo.server.user.vo.UserResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(
    "/api/v1/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class UserControllerV1(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase,
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val listRelationshipUseCase: ListRelationshipUseCase
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

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:write')")
    @PatchMapping
    fun patchUser(authentication: Authentication, @RequestBody @Valid @NotNull request: PatchUserRequest?) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        patchUserUseCase.execute(PatchUserUseCase.Params(jwtClaim, request!!))
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:read')")
    @GetMapping("/search", params = ["q"])
    fun searchUsers(
        principal: Principal?,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(SearchUserUseCase.Params(query, cursor))
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:write')")
    @PutMapping("/following/{userId}")
    fun follow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        followUseCase.execute(jwtClaim, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:write')")
    @DeleteMapping("/following/{userId}")
    fun unfollow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        unfollowUseCase.execute(jwtClaim, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:read')")
    @GetMapping("/{userId}/followers")
    fun listFollowers(
        authentication: Authentication,
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(ListRelationshipUseCase.Type.FOLLOWER, userId, cursor)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:read')")
    @GetMapping("/{userId}/following")
    fun listFollowing(
        authentication: Authentication,
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(ListRelationshipUseCase.Type.FOLLOWING, userId, cursor)
    }
}