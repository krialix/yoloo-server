package com.yoloo.server.user.config

//@EnableAuthorizationServer
//@Configuration
class Oauth2AuthorizationServerConfig/*(
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
        val chain = TokenEnhancerChain()
        chain.setTokenEnhancers(listOf(CustomTokenEnhancer(), accessTokenConverter()))

        endpoints
            .prefix("/api/v1/")
            *//*.pathMapping("/oauth/authorize", "/api/v1/oauth/authorize")
            .pathMapping("/oauth/token", "/api/v1/oauth/token")
            .pathMapping("/oauth/check_token", "/api/v1/oauth/check_token")
            .pathMapping("/oauth/confirm_access", "/api/v1/oauth/confirm_access")
            .pathMapping("/oauth/error", "/api/v1/oauth/error")
            .pathMapping("/oauth/token_key", "/api/v1/oauth/token_key")*//*
            .authenticationManager(authenticationManager)
            .tokenStore(tokenStore())
            .tokenEnhancer(chain)
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
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val keyFactory = getKeyStoreKeyFactory()
        converter.setKeyPair(keyFactory.getKeyPair("jwt"))
        return converter
    }

    private fun getKeyStoreKeyFactory(): KeyStoreKeyFactory {
        return KeyStoreKeyFactory(ClassPathResource("yoloo-server-jwt.jks"), "yolooisnewwress".toCharArray())
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    class CustomTokenEnhancer : TokenEnhancer {

        override fun enhance(
            accessToken: OAuth2AccessToken,
            authentication: OAuth2Authentication
        ): OAuth2AccessToken {
            val user = authentication.principal as Oauth2User

            val info = mapOf(
                "sub" to user.userId,
                "email" to user.email,
                "picture" to user.profileImageUrl
            )

            (accessToken as DefaultOAuth2AccessToken).additionalInformation = info

            return accessToken
        }
    }
}*/