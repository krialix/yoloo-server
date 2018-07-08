package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserResponse(
    val id: Long,
    val profileUrl: String?,
    val displayName: String,
    val self: Boolean,
    val following: Boolean,
    val profileImageUrl: String,
    val email: String,
    val about: String?,
    val website: String?,
    val count: UserCountResponse,
    val locale: LocaleResponse,
    val subscribedGroups: List<UserGroupResponse>,
    val spokenLanguages: List<LanguageResponse>
)