package com.yoloo.server.post.infrastructure

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.yoloo.server.post.infrastructure.mapper.PostCollectionResponseMapper
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
class PostServiceAutoConfiguration {

    @Bean
    fun etagFilterRegistration(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val registration = FilterRegistrationBean<ShallowEtagHeaderFilter>(ShallowEtagHeaderFilter())
        registration.addUrlPatterns("/api/*")
        return registration
    }

    @Bean
    fun memcacheService(): MemcacheService {
        return MemcacheServiceFactory.getMemcacheService()
    }

    @Bean
    fun asyncMemcacheService(): AsyncMemcacheService {
        return MemcacheServiceFactory.getAsyncMemcacheService()
    }

    @Bean
    fun postCollectionResponseMapper(): PostCollectionResponseMapper {
        return PostCollectionResponseMapper()
    }
}