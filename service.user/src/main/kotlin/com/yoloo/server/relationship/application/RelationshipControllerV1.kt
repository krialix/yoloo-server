package com.yoloo.server.relationship.application

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.relationship.domain.usecase.DeleteRelationshipUseCase
import com.yoloo.server.relationship.domain.usecase.InsertRelationshipUseCase
import com.yoloo.server.relationship.domain.usecase.contract.DeleteRelationshipContract
import com.yoloo.server.relationship.domain.usecase.contract.InsertRelationshipContract
import com.yoloo.server.user.domain.usecase.ListRelationshipUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping(
    "/api/v1/users/relationships",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class RelationshipControllerV1 @Autowired constructor(
    private val insertRelationshipUseCase: InsertRelationshipUseCase,
    private val deleteRelationshipUseCase: DeleteRelationshipUseCase,
    private val listRelationshipUseCase: ListRelationshipUseCase
) {

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun insertRelationship(principal: Principal, @PathVariable("userId") userId: String) {
        insertRelationshipUseCase.execute(InsertRelationshipContract.Request(principal, userId))
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun deleteRelationship(principal: Principal, @PathVariable("userId") userId: String) {
        deleteRelationshipUseCase.execute(DeleteRelationshipContract.Request(principal, userId))
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