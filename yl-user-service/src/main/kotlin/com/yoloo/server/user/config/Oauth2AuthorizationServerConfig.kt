package com.yoloo.server.user.config

import com.yoloo.server.user.vo.Oauth2User
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.token.AccessTokenProvider
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory

@EnableAuthorizationServer
@Configuration
class Oauth2AuthorizationServerConfig(
    @Qualifier("authenticationManagerBean")
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
) : AuthorizationServerConfigurerAdapter() {

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
            .withClient("trusted_mobile")
            .authorizedGrantTypes("password", "refresh_token")
            .secret(passwordEncoder.encode("secret"))
            .scopes("user:read", "user:write")
            .and()
            .withClient("anon_mobile")
            .authorizedGrantTypes("client_credentials")
            .secret(passwordEncoder.encode("secret"))
            .scopes("global_feed", "groups:read", "post:read")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .pathMapping("/oauth/authorize", "/api/v1/oauth/authorize")
            .pathMapping("/oauth/token", "/api/v1/oauth/token")
            .pathMapping("/oauth/check_token", "/api/v1/oauth/check_token")
            .pathMapping("/oauth/confirm_access", "/api/v1/oauth/confirm_access")
            .pathMapping("/oauth/error", "/api/v1/oauth/error")
            .pathMapping("/oauth/token_key", "/api/v1/oauth/token_key")
            .authenticationManager(authenticationManager)
            .tokenStore(tokenStore())
            .tokenEnhancer(jwtAccessTokenConverter())
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
    }

    @Bean
    fun resourceOwnerPasswordResourceDetails(): ResourceOwnerPasswordResourceDetails {
        val passwordResourceDetails = ResourceOwnerPasswordResourceDetails()
        passwordResourceDetails.clientId = "trusted_mobile"
        passwordResourceDetails.clientSecret = "secret"
        passwordResourceDetails.accessTokenUri = "http://localhost:8080/api/v1/oauth/token"
        return passwordResourceDetails
    }

    @Bean
    fun resourceOwnerPasswordAccessTokenProvider(): AccessTokenProvider {
        return ResourceOwnerPasswordAccessTokenProvider()
    }

    @Bean
    fun jwtAccessTokenConverter(): JwtAccessTokenConverter {
        val converter = CustomTokenEnhancer()
        val keyFactory =
            KeyStoreKeyFactory(ClassPathResource("yoloo-server-jwt.jks"), "yolooisnewwress".toCharArray())
        converter.setKeyPair(keyFactory.getKeyPair("jwt"))
        return converter
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(jwtAccessTokenConverter())
    }

    class CustomTokenEnhancer : JwtAccessTokenConverter() {

        override fun enhance(
            accessToken: OAuth2AccessToken,
            authentication: OAuth2Authentication
        ): OAuth2AccessToken {
            val user = authentication.principal as Oauth2User

            val info = LinkedHashMap(accessToken.additionalInformation)
            info["sub"] = user.userId
            info["email"] = user.email
            info["picture"] = user.profileImageUrl

            val customAccessToken = DefaultOAuth2AccessToken(accessToken)
            customAccessToken.additionalInformation = info

            return super.enhance(customAccessToken, authentication)
        }
    }
}