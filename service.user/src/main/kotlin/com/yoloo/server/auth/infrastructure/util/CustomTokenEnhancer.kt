package com.yoloo.server.auth.infrastructure.util

import com.yoloo.server.auth.domain.vo.OauthUserDetails
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.stereotype.Component

@Component
class CustomTokenEnhancer : TokenEnhancer {

    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {
        val details = authentication.principal as OauthUserDetails

        val map = mapOf<String, Any>("userId" to details.userId, "email" to details.email)
        val token = accessToken as DefaultOAuth2AccessToken
        token.additionalInformation = map

        return token
    }
}