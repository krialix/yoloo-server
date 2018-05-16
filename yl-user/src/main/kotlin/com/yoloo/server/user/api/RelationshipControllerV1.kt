package com.yoloo.server.user.api

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.user.usecase.FollowUseCase
import com.yoloo.server.user.usecase.ListRelationshipUseCase
import com.yoloo.server.user.usecase.UnfollowUseCase
import com.yoloo.server.user.vo.RelationshipResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/relationships",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class RelationshipControllerV1(
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val listRelationshipUseCase: ListRelationshipUseCase
) {

    @PostMapping("/follow/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun follow(principal: Principal?, @PathVariable("userId") userId: Long) {
        followUseCase.execute(FollowUseCase.Request(principal, userId))
    }

    @DeleteMapping("/unfollow/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unfollow(principal: Principal?, @PathVariable("userId") userId: Long) {
        unfollowUseCase.execute(UnfollowUseCase.Request(principal, userId))
    }

    @GetMapping("/{userId}/followers")
    fun listFollowers(
        principal: Principal?,
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(
            ListRelationshipUseCase.Request(
                ListRelationshipUseCase.Type.FOLLOWER,
                userId,
                cursor
            )
        )
    }

    @GetMapping("/{userId}/followings")
    fun listFollowings(
        principal: Principal?,
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listRelationshipUseCase.execute(
            ListRelationshipUseCase.Request(
                ListRelationshipUseCase.Type.FOLLOWING,
                userId,
                cursor
            )
        )
    }
}