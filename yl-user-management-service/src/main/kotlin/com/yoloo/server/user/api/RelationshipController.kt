package com.yoloo.server.user.api

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.user.usecase.*
import com.yoloo.server.user.vo.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(
    "/api/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class RelationshipController(
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val listRelationshipUseCase: ListRelationshipUseCase
) {

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PutMapping("/following/{userId}")
    fun follow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        followUseCase.execute(jwtClaim.sub, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @DeleteMapping("/following/{userId}")
    fun unfollow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unfollowUseCase.execute(jwtClaim.sub, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/{userId}/followers")
    fun listFollowers(
        authentication: Authentication,
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(
            ListRelationshipUseCase.Type.FOLLOWER,
            userId,
            cursor
        )
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/{userId}/following")
    fun listFollowing(
        authentication: Authentication,
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(
            ListRelationshipUseCase.Type.FOLLOWING,
            userId,
            cursor
        )
    }
}