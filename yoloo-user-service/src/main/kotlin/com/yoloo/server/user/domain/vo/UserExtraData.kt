package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.RegexUtil
import javax.validation.constraints.Pattern

@NoArg
data class UserExtraData(
    private var password: String,

    @field:Pattern(regexp = RegexUtil.IP_REGEXP, message = "users-4")
    private var lastKnownIP: String,

    private var fcmToken: String,

    private var expired: Boolean = false,

    private var credentialsExpired: Boolean = false,

    private var locked: Boolean = false,

    private var enabled: Boolean = true
)