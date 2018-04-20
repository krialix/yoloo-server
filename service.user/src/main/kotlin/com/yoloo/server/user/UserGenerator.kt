package com.yoloo.server.user

import com.yoloo.server.common.util.TimestampIdGenerator
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.vo.*
import java.util.*

object UserGenerator {

    fun generateUsers(): List<*> {
        return (1..5).map {
            User(
                displayName = UserDisplayName(value = "Demo $it"),
                email = Email("demo$it@demo.com"),
                image = AvatarImage(""),
                fcmToken = UUID.randomUUID().toString(),
                scopes = setOf("user:read", "user:write"),
                lastKnownIP = IP("127.0.0.1"),
                locale = UserLocale(language = Locale.ENGLISH.language, country = "en_US"),
                subscribedGroups = (1..3).map {
                    UserGroup(
                        id = TimestampIdGenerator.generateId(),
                        imageUrl = "",
                        displayName = "group-$it"
                    )
                }
            )
        }
    }
}