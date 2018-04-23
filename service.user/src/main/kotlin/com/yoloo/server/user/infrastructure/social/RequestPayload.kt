package com.yoloo.server.user.infrastructure.social

data class RequestPayload(
    val clientId: String,
    val subscribedGroupIds: List<String>,
    val token: String?,
    val displayName: String?,
    val email: String?,
    val password: String?
)