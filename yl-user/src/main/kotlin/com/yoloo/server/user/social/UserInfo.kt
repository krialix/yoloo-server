package com.yoloo.server.user.social

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserInfo(
    val providerId: String?,
    val picture: String
)