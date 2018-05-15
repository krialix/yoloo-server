package com.yoloo.server.post.infrastructure

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
class PostServiceConfiguration {

    @Bean
    fun etagFilterRegistration(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val registration = FilterRegistrationBean<ShallowEtagHeaderFilter>(ShallowEtagHeaderFilter())
        registration.addUrlPatterns("/api/*")
        return registration
    }
}