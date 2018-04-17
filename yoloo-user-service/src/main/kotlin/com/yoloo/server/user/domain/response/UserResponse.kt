package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserResponse(
    val id: String,
    val url: String?,
    val displayName: String,
    val self: Boolean,
    val following: Boolean,
    val avatarUrl: String,
    val email: String,
    val about: String?,
    val website: String?,
    val count: UserCountResponse,
    val locale: LocaleResponse,
    val subscribedGroups: List<UserGroupResponse>
)