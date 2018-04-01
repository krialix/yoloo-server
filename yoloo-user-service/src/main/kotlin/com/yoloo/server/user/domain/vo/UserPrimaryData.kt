package com.yoloo.server.user.domain.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.RegexUtil
import com.yoloo.server.user.domain.vo.Email
import com.yoloo.server.user.domain.vo.UserDisplayName
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Pattern

@NoArg
data class UserPrimaryData(
    @field:Valid
    private var displayName: UserDisplayName,

    @field:Valid
    private var email: Email,

    private var avatarUrl: String,

    private var password: String? = null,

    @field:Pattern(regexp = RegexUtil.IP_REGEXP, message = "users-4")
    private var lastKnownIP: String,

    private var fcmToken: String,

    private var expired: Boolean = false,

    private var credentialsExpired: Boolean = false,

    private var locked: Boolean = false,

    private var enabled: Boolean = true,

    private var scopes: Set<String>,

    private var createdAt: LocalDateTime = LocalDateTime.now(),

    private var updatedAt: LocalDateTime = createdAt,

    @Index
    private var deletedAt: LocalDateTime? = null
)