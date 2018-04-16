package com.yoloo.server.user.application

import com.yoloo.server.common.util.RestMediaType
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.UserGenerator
import com.yoloo.server.user.domain.request.PatchUserRequest
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.PatchUserUseCase
import com.yoloo.server.user.domain.usecase.SearchUserUseCase
import com.yoloo.server.user.domain.usecase.contract.GetUserUseCaseContract
import com.yoloo.server.user.domain.usecase.contract.PatchUserUseCaseContract
import com.yoloo.server.user.domain.usecase.contract.SearchUserUseCaseContract
import org.dialectic.jsonapi.response.DataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping(
    "/api/v1/users",
    produces = [RestMediaType.APPLICATION_API_JSON_VND_VALUE],
    consumes = [RestMediaType.APPLICATION_API_JSON_VND_VALUE]
)
class UserControllerV1 @Autowired constructor(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase
) {

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getUser(principal: Principal, @PathVariable("userId") userId: String): DataResponse<UserResponse> {
        return getUserUseCase.execute(GetUserUseCaseContract.Request(principal, userId)).response
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun insertUser() {
        // todo get user avatar from social media or gravatar
        // todo save new user to db
        // todo increase group counts via user following group list
        // todo create user feed task and populate feed with group posts
        ofy().save().entities(UserGenerator.generate()).now()
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun patchUser(
        principal: Principal,
        @PathVariable("userId") userId: String,
        @RequestBody @Valid patchUserRequest: PatchUserRequest
    ) {
        patchUserUseCase.execute(PatchUserUseCaseContract.Request(principal, userId, patchUserRequest))
    }

    @GetMapping("/search", params = ["q"])
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun searchUsers(
        @RequestParam("q") query: String,
        @RequestParam(value = "limit", required = false, defaultValue = "20") limit: Int,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): DataResponse<SearchUserResponse> {
        return searchUserUseCase.execute(SearchUserUseCaseContract.Request(query, limit, cursor)).response
    }
}