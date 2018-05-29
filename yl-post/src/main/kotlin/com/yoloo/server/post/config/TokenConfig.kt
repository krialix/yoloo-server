package com.yoloo.server.post.config

import com.yoloo.server.post.vo.JwtClaims
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import java.util.function.Function

@Configuration
class TokenConfig {

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.accessTokenConverter = JwtConverter()
        val publicKey =
            ClassPathResource("public.txt").inputStream.bufferedReader().use { it.readText() }
        converter.setVerifierKey(publicKey)
        return converter
    }

    @Bean
    fun tokenStore(jwtAccessTokenConverter: JwtAccessTokenConverter): TokenStore {
        return JwtTokenStore(jwtAccessTokenConverter)
    }

    class JwtConverter : DefaultAccessTokenConverter(), JwtAccessTokenConverterConfigurer {

        override fun configure(converter: JwtAccessTokenConverter) {
            converter.accessTokenConverter = this
        }

        override fun extractAuthentication(map: Map<String, *>): OAuth2Authentication {
            val auth = super.extractAuthentication(map)
            auth.details = JwtClaimMapper.apply(map)
            return auth
        }

        companion object {
            object JwtClaimMapper : Function<Map<String, *>, JwtClaims> {
                override fun apply(t: Map<String, *>): JwtClaims {
                    return JwtClaims(
                        sub = t["sub"] as Long,
                        displayName = t["user_name"] as String,
                        picture = t["picture"] as String,
                        email = t["email"] as String,
                        scopes = t["authorities"] as List<String>,
                        jti = t["jti"] as String,
                        exp = t["exp"] as Long,
                        clientId = t["client_id"] as String,
                        fcmToken = t["fcm_token"] as String
                    )
                }
            }
        }
    }
}