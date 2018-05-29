package com.yoloo.server.auth.util

import com.yoloo.server.auth.vo.Oauth2User
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.stereotype.Component

@Component
class CustomTokenEnhancer : TokenEnhancer {

    override fun enhance(
        accessToken: OAuth2AccessToken,
        authentication: OAuth2Authentication
    ): OAuth2AccessToken {
        val user = authentication.principal as Oauth2User
        val token = accessToken as DefaultOAuth2AccessToken

        val map = mapOf(
            "sub" to user.userId,
            "email" to user.email,
            "picture" to user.profileImageUrl,
            "fcm_token" to user.fcmToken
        )

        token.additionalInformation = map

        return token
    }
}