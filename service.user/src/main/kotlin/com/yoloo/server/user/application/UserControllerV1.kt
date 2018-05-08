package com.yoloo.server.user.application

import com.yoloo.server.auth.domain.usecase.InsertUserUseCase
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.requestpayload.PatchUserPayload
import com.yoloo.server.user.domain.response.SearchUserResponse
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.PatchUserUseCase
import com.yoloo.server.user.domain.usecase.SearchUserUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@ResponseBody
@RequestMapping(
    "/api/v1/users",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class UserControllerV1 @Autowired constructor(
    private val getUserUseCase: GetUserUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val patchUserUseCase: PatchUserUseCase,
    private val insertUserUseCase: InsertUserUseCase
) {
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(principal: Principal?, @PathVariable("userId") userId: Long): UserResponse {
        principal?.let { println(it) }
        principal?.let { println(it.javaClass.name) }
        return getUserUseCase.execute(GetUserUseCase.Request(principal, userId))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertUser(@RequestBody @Valid payload: InsertUserPayload): UserResponse {
        return insertUserUseCase.execute(InsertUserUseCase.Request(payload))
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    fun patchUser(
        principal: Principal?,
        @RequestBody @Valid payload: PatchUserPayload
    ) {
        patchUserUseCase.execute(PatchUserUseCase.Request(principal, payload))
    }

    @GetMapping("/search", params = ["q"])
    @ResponseStatus(HttpStatus.OK)
    fun searchUsers(
        principal: Principal?,
        @RequestParam("q") query: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SearchUserResponse> {
        return searchUserUseCase.execute(SearchUserUseCase.Request(query, cursor))
    }
}