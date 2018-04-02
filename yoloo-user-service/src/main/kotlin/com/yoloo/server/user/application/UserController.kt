package com.yoloo.server.user.application

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.UserGenerator
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController {

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable("userId") userId: String): ResponseEntity<UserResponse>? {
        val user = ofy().load().type(User::class.java).id(userId).now()

        val selfLink = linkTo(UserController::class.java).slash(user.id).withSelfRel()

        val userResponse = UserResponse(
            id = user.id,
            displayName = user.userPrimaryData.displayName.value,
            avatarUrl = user.userPrimaryData.avatarUrl,
            email = user.userPrimaryData.email.value,
            createdAt = user.userPrimaryData.createdAt,
            bio = user.userSecondaryData.bio,
            website = user.userSecondaryData.website
        )

        userResponse.add(selfLink)
        return ResponseEntity.ok(userResponse)
    }

    @GetMapping("/users2")
    fun insertUser() {
        ofy().save().entities(UserGenerator.generate()).now()
    }

    @GetMapping("/users3")
    fun listUser(): ResponseEntity<List<User>> {

        val list = ofy().load().type(User::class.java).orderKey(true).list()

        return ResponseEntity.ok(list)
    }
}