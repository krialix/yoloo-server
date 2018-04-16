package com.yoloo.server.user

import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.vo.AvatarUrl
import com.yoloo.server.user.domain.vo.Email
import com.yoloo.server.user.domain.vo.UserDisplayName
import java.util.*

object UserGenerator {

    fun generate(): List<User> {
        return (1..5).map {
            User(
                displayName = UserDisplayName(value = "Demo $it"),
                email = Email("demo$it@demo.com"),
                avatarUrl = AvatarUrl(""),
                fcmToken = "",
                scopes = setOf("user:read", "user:write"),
                lastKnownIP = "127.0.0.1",
                locale = Locale.ENGLISH,
                countryIsoCode = "en"
            )
        }
    }
}