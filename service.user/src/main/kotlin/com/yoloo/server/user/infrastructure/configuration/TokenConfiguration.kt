package com.yoloo.server.user.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory

@Configuration
class TokenConfiguration {

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val keyFactory = KeyStoreKeyFactory(ClassPathResource("jwt.jks"), "emakinatalent".toCharArray())
        converter.setKeyPair(keyFactory.getKeyPair("jwt"))
        return converter
    }

    @Bean
    fun tokenStore(jwtAccessTokenConverter: JwtAccessTokenConverter): TokenStore {
        return JwtTokenStore(jwtAccessTokenConverter)
    }

    @Bean
    fun tokenServices(tokenStore: TokenStore, tokenEnhancer: TokenEnhancer): DefaultTokenServices {
        return DefaultTokenServices().apply {
            setTokenStore(tokenStore)
            setSupportRefreshToken(true)
            setTokenEnhancer(tokenEnhancer)
        }
    }
}