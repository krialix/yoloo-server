package com.yoloo.server.relationship.application

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.relationship.domain.usecase.FollowUseCase
import com.yoloo.server.relationship.domain.usecase.UnfollowUseCase
import com.yoloo.server.user.domain.usecase.ListRelationshipUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/relationships",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class RelationshipControllerV1 @Autowired constructor(
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val listRelationshipUseCase: ListRelationshipUseCase
) {

    @PutMapping("/follow/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun follow(principal: Principal, @PathVariable("userId") userId: Long) {
        followUseCase.execute(FollowUseCase.Request(principal, userId))
    }

    @DeleteMapping("/unfollow/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun unfollow(principal: Principal, @PathVariable("userId") userId: Long) {
        unfollowUseCase.execute(UnfollowUseCase.Request(principal, userId))
    }

    @GetMapping("/{userId}/followers")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
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
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
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