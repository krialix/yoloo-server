package com.yoloo.server.core.user.infrastructure

import com.google.appengine.api.datastore.Email
import com.googlecode.objectify.Key
import com.yoloo.server.core.objectify.OfyService.Companion.factory
import com.yoloo.server.core.objectify.OfyService.Companion.ofy
import com.yoloo.server.core.user.domain.User
import com.yoloo.server.core.user.domain.UserDisplayName
import com.yoloo.server.core.user.domain.UserGroup
import com.yoloo.server.core.user.domain.UserMeta
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

@RestController
class UserController {

    @GetMapping
    fun getUser(): User {
        val id = factory().allocateId(User::class.java).id

        val user = User(
            id = id,
            displayName = UserDisplayName(displayName = "Yasin Sinan Kayacan"),
            avatarUrl = "",
            bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pharetra mauris leo, in tempus odio eleifend nec.",
            locale = Locale.ENGLISH,
            userMeta = UserMeta(password = "", lastKnownIP = ""),
            email = Email("y.kayacan@emakina.com.tr"),
            scopes = listOf("user:read", "user:write"),
            birthDate = LocalDate.now(),
            countryIsoCode = "",
            website = "",
            subscribedGroups = listOf(UserGroup(id = 10, displayName = "group1"))
        )

        ofy().save().entity(user).now()

        return ofy().load().key(Key.create(user)).now()
    }
}