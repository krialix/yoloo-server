package com.yoloo.server.user

import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.vo.Email
import com.yoloo.server.user.domain.vo.UserDisplayName
import com.yoloo.server.user.domain.vo.UserPrimaryData
import com.yoloo.server.user.domain.vo.UserSecondaryData
import java.util.*

object UserGenerator {

    fun generate(): List<User> {
        return (1..5).map {
            User(
                userPrimaryData = UserPrimaryData(
                    displayName = UserDisplayName(value = "Demo $it"),
                    email = Email("demo$it@demo.com"),
                    avatarUrl = "",
                    fcmToken = "",
                    scopes = setOf("user:read", "user:write"),
                    lastKnownIP = "127.0.0.1"
                ),
                userSecondaryData = UserSecondaryData(
                    locale = Locale.ENGLISH,
                    countryIsoCode = "en"
                )
            )
        }
    }
}