package com.yoloo.server.common.security

import com.yoloo.server.auth.BaseWebSecurityConfig
import com.yoloo.server.auth.firebase.EnableFirebaseSecurity
import com.yoloo.server.auth.firebase.FirebaseAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableFirebaseSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    firebaseAuthenticationProvider: FirebaseAuthenticationProvider
) : BaseWebSecurityConfig(firebaseAuthenticationProvider) {

    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http
            .authorizeRequests()
            .antMatchers("/api/users/signUp", "/api/users/checkEmail", "/_ah/**", "/api/test/pubsub/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/api/**")
            .authenticated()
    }
}