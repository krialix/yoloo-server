package com.yoloo.server.auth.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.client.token.AccessTokenProvider
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenEnhancer

@EnableAuthorizationServer
@Configuration
class Oauth2AuthorizationServerConfig(
    private val authenticationManager: AuthenticationManager,
    private val tokenEnhancer: TokenEnhancer,
    private val scopeProperties: ScopeProperties
) : AuthorizationServerConfigurerAdapter() {

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
            .withClient("trusted_mobile")
            .authorizedGrantTypes("password", "refresh_token")
            .secret("secret")
            .scopes(*scopeProperties.scopesArray)
            .and()
            .withClient("anon_mobile")
            .authorizedGrantTypes("client_credentials")
            .secret("secret")
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
            .tokenEnhancer(tokenEnhancer)
    }

    @Bean
    fun resourceOwnerPasswordResourceDetails(): ResourceOwnerPasswordResourceDetails {
        val passwordResourceDetails = ResourceOwnerPasswordResourceDetails()
        passwordResourceDetails.clientId = "trusted_mobile"
        passwordResourceDetails.clientSecret = "secret"
        passwordResourceDetails.accessTokenUri = "http://localhost:8081/api/v1/oauth/token"
        return passwordResourceDetails
    }

    @Bean
    fun resourceOwnerPasswordAccessTokenProvider(): AccessTokenProvider {
        return ResourceOwnerPasswordAccessTokenProvider()
    }
}