package com.yoloo.server.post.vo

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails

data class JwtClaims(
    val sub: Long,
    val displayName: String,
    val picture: String,
    val email: String,
    val scopes: List<String>,
    val jti: String,
    val exp: Long,
    val clientId: String,
    val fcmToken: String
) {

    companion object {
        fun from(authentication: Authentication): JwtClaims {
            val details = authentication.details as OAuth2AuthenticationDetails
            return details.decodedDetails as JwtClaims
        }
    }
}