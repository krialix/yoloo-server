package com.yoloo.server.user.application

import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.user.domain.request.InsertUserRequest
import com.yoloo.server.user.domain.request.PatchUserRequest
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.InsertUserUseCase
import com.yoloo.server.user.domain.usecase.PatchUserUseCase
import com.yoloo.server.user.domain.usecase.SearchUserUseCase
import com.yoloo.server.user.domain.usecase.contract.GetUserContract
import com.yoloo.server.user.domain.usecase.contract.InsertUserContract
import com.yoloo.server.user.domain.usecase.contract.PatchUserContract
import com.yoloo.server.user.domain.usecase.contract.SearchUserContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping(
    "/api/v1/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class UserControllerV1 @Autowired constructor(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase,
    private val insertUserUseCase: InsertUserUseCase
) {

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getUser(principal: Principal?, @PathVariable("userId") userId: String): UserResponse {
        return getUserUseCase.execute(GetUserContract.Request(principal, userId)).response
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun insertUser(@RequestBody @Valid request: InsertUserRequest): UserResponse {
        return insertUserUseCase.execute(InsertUserContract.Request(request)).response
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun patchUser(
        principal: Principal?,
        @PathVariable("userId") userId: String,
        @RequestBody @Valid request: PatchUserRequest
    ) {
        patchUserUseCase.execute(PatchUserContract.Request(principal, userId, request))
    }

    @GetMapping("/search", params = ["q"])
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun searchUsers(
        principal: Principal?,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(SearchUserContract.Request(query, cursor)).response
    }
}