package com.yoloo.server.user.application

import com.yoloo.server.common.util.RestMediaType
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.UserGenerator
import com.yoloo.server.user.domain.request.PatchUserRequest
import com.yoloo.server.user.domain.response.FollowerResponse
import com.yoloo.server.user.domain.response.FollowingResponse
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.*
import com.yoloo.server.user.domain.usecase.contract.*
import org.dialectic.jsonapi.response.DataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(
    "/api/v1/users",
    produces = [RestMediaType.APPLICATION_API_JSON_VND_VALUE],
    consumes = [RestMediaType.APPLICATION_API_JSON_VND_VALUE]
)
class UserControllerV1 @Autowired constructor(
    private val getUserUseCase: GetUserUseCase,
    private val listFollowersUseCase: ListFollowersUseCase,
    private val listFollowingsUseCase: ListFollowingsUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val patchUserUseCase: PatchUserUseCase
) {

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getUser(@PathVariable("userId") userId: String): DataResponse<UserResponse> {
        return getUserUseCase.execute(GetUserUseCaseContract.Request(userId)).response
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
    fun patchUser(@PathVariable("userId") userId: String, @RequestBody @Valid patchUserRequest: PatchUserRequest) {
        patchUserUseCase.execute(PatchUserUseCaseContract.Request(userId, patchUserRequest))
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    fun deleteUser(@PathVariable("userId") userId: String) {
        deleteUserUseCase.execute(DeleteUserUseCaseContract.Request(userId))
    }

    @GetMapping("/{userId}/followers")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun listFollowers(
        @PathVariable("userId") userId: String,
        @RequestParam(value = "limit", required = false, defaultValue = "20") limit: Int,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): DataResponse<FollowerResponse> {
        return listFollowersUseCase.execute(ListFollowersUseCaseContract.Request(userId, limit, cursor)).response
    }

    @GetMapping("/{userId}/followings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun listFollowings(
        @PathVariable("userId") userId: String,
        @RequestParam(value = "limit", required = false, defaultValue = "20") limit: Int,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): DataResponse<FollowingResponse> {
        return listFollowingsUseCase.execute(ListFollowingsUseCaseContract.Request(userId, limit, cursor)).response
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