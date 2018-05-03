package com.yoloo.server.user.domain.vo

import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.condition.IfFalse
import com.yoloo.server.common.util.NoArg
import java.time.LocalDateTime

@NoArg
data class Account(
    var provider: SocialProvider,

    var email: Email,

    var password: Password? = null,

    var lastKnownIP: IP,

    var fcmToken: String,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    @Index(IfFalse::class)
    var enabled: Boolean = true,

    var scopes: Set<String>,

    var lastLoginTime: LocalDateTime? = null,

    var deletedAt: LocalDateTime? = null
)