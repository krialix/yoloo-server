package com.yoloo.server.user.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
class ResourceServerConfiguration(private val tokenStore: TokenStore) : ResourceServerConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .authorizeRequests()
            .antMatchers("/_ah/**")
            .permitAll()
            .and()
            .httpBasic()
            .disable()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.tokenStore(tokenStore)
    }
}