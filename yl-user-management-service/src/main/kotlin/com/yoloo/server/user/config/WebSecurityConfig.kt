package com.yoloo.server.user.config

import com.yoloo.server.user.service.Oauth2UserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class WebSecurityConfig(
    private val oauth2UserDetailsService: Oauth2UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

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
            .antMatchers("/api/v1/users/checkEmail", "/api/v1/users/signUp", "/api/v1/admin/**", "/_ah/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/api/**")
            .authenticated()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(oauth2UserDetailsService).passwordEncoder(passwordEncoder)
    }
}