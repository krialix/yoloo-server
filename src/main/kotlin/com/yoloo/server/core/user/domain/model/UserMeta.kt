package com.yoloo.server.core.user.domain.model

import com.yoloo.server.core.util.NoArg
import javax.validation.constraints.Pattern

@NoArg
data class UserMeta(
    var password: String,

    @get:Pattern(regexp = IP_REGEXP)
    var lastKnownIP: String,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var enabled: Boolean = true
) {

    companion object {
        private const val zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])"

        private const val IP_REGEXP = "$zeroTo255\\.$zeroTo255\\.$zeroTo255\\.$zeroTo255"
    }
}