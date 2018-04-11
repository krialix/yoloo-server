package com.yoloo.server.user.application

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.UserGenerator
import com.yoloo.server.user.domain.request.GetUserRequest
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import org.dialectic.jsonapi.response.DataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users", produces = ["application/vnd.api+json"], consumes = ["application/vnd.api+json"])
class UserControllerV1 @Autowired constructor(private val getUserUseCase: GetUserUseCase) {

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getUser(@PathVariable("userId") userId: String): DataResponse<UserResponse> {
        return getUserUseCase.execute(GetUserRequest(userId)).response
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
}