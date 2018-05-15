package com.yoloo.server.auth.domain.vo

data class JwtClaims(
    val sub: Long,
    val displayName: String,
    val profileImageUrl: String,
    val email: String,
    val scopes: List<String>,
    val jti: String,
    val exp: Long,
    val clientId: String
)