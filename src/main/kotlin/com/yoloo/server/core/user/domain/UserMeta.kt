package com.yoloo.server.core.user.domain

import com.yoloo.server.core.util.NoArg

@NoArg
data class UserMeta(
    val password: String,
    val lastKnownIP: String,
    val expired: Boolean = false,
    val credentialsExpired: Boolean = false,
    val locked: Boolean = false,
    val enabled: Boolean = true
)