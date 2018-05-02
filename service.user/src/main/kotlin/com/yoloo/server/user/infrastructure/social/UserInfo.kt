package com.yoloo.server.user.infrastructure.social

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserInfo(
    val providerId: String?,
    val providerType: ProviderType,
    val picture: String
)