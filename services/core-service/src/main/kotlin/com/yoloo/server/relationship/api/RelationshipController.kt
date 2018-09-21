package com.yoloo.server.relationship.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.relationship.usecase.FollowUseCase
import com.yoloo.server.relationship.usecase.ListRelationshipUseCase
import com.yoloo.server.relationship.usecase.UnfollowUseCase
import com.yoloo.server.user.vo.RelationshipResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    "/api/users/relationships",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class RelationshipController(
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val listRelationshipUseCase: ListRelationshipUseCase
) {

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PutMapping("/{userId}")
    fun follow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val user = AuthUtil.from(authentication)

        followUseCase.execute(user.userId, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @DeleteMapping("/{userId}")
    fun unfollow(authentication: Authentication, @PathVariable("userId") userId: Long) {
        val user = AuthUtil.from(authentication)

        unfollowUseCase.execute(user.userId, userId)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/{userId}/followers")
    fun listFollowers(
        authentication: Authentication,
        @PathVariable("userId") userId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(Relationship.Type.FOLLOWER, userId, cursor)
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/{userId}/following")
    fun listFollowing(
        authentication: Authentication,
        @PathVariable("userId") userId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(Relationship.Type.FOLLOWING, userId, cursor)
    }
}