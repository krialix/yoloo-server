package com.yoloo.server.post.vo

data class JwtClaims(
    val sub: Long,
    val displayName: String,
    val picture: String,
    val email: String,
    val scopes: List<String>,
    val jti: String,
    val exp: Long,
    val clientId: String,
    val updatedAt: String,
    val fcmToken: String
)