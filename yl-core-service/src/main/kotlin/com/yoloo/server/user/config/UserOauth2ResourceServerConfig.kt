package com.yoloo.server.user.config

/*
@Configuration class UserOauth2ResourceServerConfig : Oauth2ResourceServerConfig() {

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
}*/