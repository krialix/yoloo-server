package com.yoloo.server.user.api

import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.vo.CollectionResponse
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
        val jwtClaim = JwtClaims.from(authentication)

        return getUserUseCase.execute(jwtClaim.sub, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:write')")
    @PatchMapping
    fun patchUser(authentication: Authentication, @RequestBody @Valid @NotNull request: PatchUserRequest?) {
        val jwtClaim = JwtClaims.from(authentication)

        patchUserUseCase.execute(jwtClaim.sub, request!!)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:read')")
    @GetMapping("/search", params = ["q"])
    fun searchUsers(
        authentication: Authentication,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(query, cursor)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:write')")
    @PutMapping("/following/{userId}")
    fun follow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        followUseCase.execute(jwtClaim.sub, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER') or #oauth2.hasScope('user:write')")
    @DeleteMapping("/following/{userId}")
    fun unfollow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unfollowUseCase.execute(jwtClaim.sub, userId)
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