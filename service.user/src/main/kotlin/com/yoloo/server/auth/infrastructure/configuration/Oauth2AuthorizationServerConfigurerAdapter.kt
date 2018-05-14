package com.yoloo.server.auth.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenEnhancer

@EnableAuthorizationServer
@Configuration
class Oauth2AuthorizationServerConfigurerAdapter(
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
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager).tokenEnhancer(tokenEnhancer)
    }
}