package com.yoloo.server.post.config

import com.yoloo.server.post.mapper.PostResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class PostMapperConfig {

    @Lazy
    @Bean
    fun postResponseMapper(): PostResponseMapper {
        return PostResponseMapper()
    }
}