package com.yoloo.server.relationship.application

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.relationship.domain.usecase.DeleteRelationshipUseCase
import com.yoloo.server.relationship.domain.usecase.InsertRelationshipUseCase
import com.yoloo.server.relationship.domain.usecase.contract.DeleteRelationshipContract
import com.yoloo.server.relationship.domain.usecase.contract.InsertRelationshipContract
import com.yoloo.server.user.domain.usecase.ListFollowersUseCase
import com.yoloo.server.user.domain.usecase.ListFollowingsUseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowersContract
import com.yoloo.server.user.domain.usecase.contract.ListFollowingsContract
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
    private val listFollowersUseCase: ListFollowersUseCase,
    private val listFollowingsUseCase: ListFollowingsUseCase
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
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listFollowersUseCase.execute(ListFollowersContract.Request(userId, cursor)).response
    }

    @GetMapping("/{userId}/followings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun listFollowings(
        @PathVariable("userId") userId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        return listFollowingsUseCase.execute(ListFollowingsContract.Request(userId, cursor)).response
    }
}