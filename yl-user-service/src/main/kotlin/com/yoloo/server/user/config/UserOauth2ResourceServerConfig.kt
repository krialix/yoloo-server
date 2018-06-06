package com.yoloo.server.user.config

import com.yoloo.server.oauth2.config.Oauth2ResourceServerConfig
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class UserOauth2ResourceServerConfig : Oauth2ResourceServerConfig() {

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
            .antMatchers("/api/v1/users/auth/**", "/api/v1/admin/**", "/tasks/**", "/_ah/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/api/**")
            .authenticated()
    }
}