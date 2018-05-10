package com.yoloo.server.auth.infrastructure.configuration

import com.yoloo.server.auth.infrastructure.provider.Yoloo2AuthProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AnonymousAuthenticationProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class ServerSecurityConfiguration(private val yoloo2AuthProvider: Yoloo2AuthProvider) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/**", "/tasks/**", "/_ah/**")
            .permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .authenticationProvider(yoloo2AuthProvider)
            .authenticationProvider(anonymousAuthenticationProvider())
    }

    @Qualifier("anon")
    @Bean
    fun anonymousAuthenticationProvider(): AuthenticationProvider {
        return AnonymousAuthenticationProvider("anon-key")
    }
}