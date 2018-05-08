package com.yoloo.server.auth.infrastructure.configuration

import com.yoloo.server.auth.infrastructure.util.CustomTokenEnhancer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.util.*

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
    fun tokenEnhancer(
        customTokenEnhancer: CustomTokenEnhancer,
        jwtAccessTokenConverter: JwtAccessTokenConverter
    ): TokenEnhancer {
        val tokenEnhancerChain = TokenEnhancerChain()
        tokenEnhancerChain.setTokenEnhancers(
            Collections.unmodifiableList(Arrays.asList(customTokenEnhancer, jwtAccessTokenConverter))
        )
        return tokenEnhancerChain
    }

    @Bean
    fun tokenServices(tokenStore: TokenStore, tokenEnhancer: CustomTokenEnhancer): DefaultTokenServices {
        return DefaultTokenServices().apply {
            setTokenStore(tokenStore)
            setSupportRefreshToken(true)
            setTokenEnhancer(tokenEnhancer)
        }
    }
}