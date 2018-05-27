package com.yoloo.server.post.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
class Oauth2ResourceServerConfig(private val tokenStore: TokenStore) : ResourceServerConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .httpBasic()
            .disable()
            .authorizeRequests()
            .antMatchers("/_ah/admin/**", "/api/v1/pubsub/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/api/**")
            .authenticated()
            .and()
            .authorizeRequests()
            .antMatchers("/tasks/**", "/_ah/**")
            .permitAll()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.tokenStore(tokenStore)
    }
}