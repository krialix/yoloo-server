package com.yoloo.server.user.application

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.UserGenerator
import com.yoloo.server.user.domain.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController {

    @GetMapping("/users")
    fun getUser(): User {
        return ofy().load().type(User::class.java).first().now()
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