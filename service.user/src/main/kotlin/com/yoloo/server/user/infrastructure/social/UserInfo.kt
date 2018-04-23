package com.yoloo.server.user.infrastructure.social

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserInfo(
    val providerId: String?,
    val providerType: ProviderType,
    val email: String,
    val picture: String,
    val displayName: String
)