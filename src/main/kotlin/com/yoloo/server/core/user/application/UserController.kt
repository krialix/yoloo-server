package com.yoloo.server.core.user.application

import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.core.user.domain.model.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/v1")
class UserController {

    @GetMapping("/users")
    fun getUser(): User {
        return ofy().load().type(User::class.java).first().now()
    }

    @GetMapping("/users2")
    fun createUser() : ResponseEntity<User> {
        val user = User(
            displayName = UserDisplayName(displayName = "Yasin Sinan Kayacan"),
            avatarUrl = "",
            locale = Locale.ENGLISH,
            userMeta = UserMeta(password = "", lastKnownIP = ""),
            email = Email("y.kayacan@emakina.com.tr"),
            scopes = setOf("user:read", "user:write"),
            birthDate = LocalDate.now(),
            countryIsoCode = "",
            subscribedGroups = listOf(UserGroup(id = 10, displayName = "group1"))
        )

        ofy().save().entity(user).now()

        return ResponseEntity.ok(user)
    }
}