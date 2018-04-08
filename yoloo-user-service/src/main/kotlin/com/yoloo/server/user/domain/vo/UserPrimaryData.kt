package com.yoloo.server.user.domain.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.RegexUtil
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Pattern

@NoArg
data class UserPrimaryData(
    var displayName: UserDisplayName,

    @field:Valid
    var email: Email,

    var avatarUrl: String,

    var password: String? = null,

    @field:Pattern(regexp = RegexUtil.IP_REGEXP, message = "users-4")
    var lastKnownIP: String,

    var fcmToken: String,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var enabled: Boolean = true,

    var scopes: Set<String>,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = createdAt,

    @Index
    var deletedAt: LocalDateTime? = null
)