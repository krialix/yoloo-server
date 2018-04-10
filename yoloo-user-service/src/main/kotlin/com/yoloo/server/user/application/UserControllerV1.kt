package com.yoloo.server.user.application

import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.UserGenerator
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserResponse
import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users", produces = ["application/vnd.api+json"], consumes = ["application/vnd.api+json"])
class UserControllerV1 {

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun getUser(@PathVariable("userId") userId: String): UserResponse {
        val user = ofy().load().type(User::class.java).id(userId).now()

        if (user == null || user.isDeleted()) {
            throw NotFoundException("user.error.not-found")
        }

        val modelMapper = ModelMapper()
        val userMap = object : PropertyMap<User, UserResponse>() {
            override fun configure() {
                map(source.displayName.value, destination.displayName)
                map(source.email.value, destination.email)
            }
        }
        modelMapper.addMappings(userMap)
        return modelMapper.map(user, UserResponse::class.java)
    }

    @PostMapping
    fun insertUser() {
        // todo get user avatar from social media or gravatar
        // todo save new user to db
        // todo increase group counts via user following group list
        // todo create user feed task and populate feed with group posts
        ofy().save().entities(UserGenerator.generate()).now()
    }
}