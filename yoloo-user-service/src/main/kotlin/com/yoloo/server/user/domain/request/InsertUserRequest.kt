package com.yoloo.server.user.domain.request

data class InsertUserRequest(
    val displayName: String,
    val email: String,
    val locale: String,
    val avatarUrl: String,
    val selectedGroupIds: List<String>
)