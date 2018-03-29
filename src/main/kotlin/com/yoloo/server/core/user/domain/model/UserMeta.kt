package com.yoloo.server.core.user.domain.model

import com.yoloo.server.core.common.util.RegexpHelper
import com.yoloo.server.core.util.NoArg
import javax.validation.constraints.Pattern

@NoArg
data class UserMeta(
    var password: String,

    @get:Pattern(regexp = RegexpHelper.IP_REGEXP)
    var lastKnownIP: String,

    var fcmToken: String,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var enabled: Boolean = true
)