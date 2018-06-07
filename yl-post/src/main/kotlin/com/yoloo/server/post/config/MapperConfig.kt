package com.yoloo.server.post.config

import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.mapper.PostResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class MapperConfig {

    @Lazy
    @Bean
    fun commentResponseMapper(): CommentResponseMapper {
        return CommentResponseMapper()
    }

    @Lazy
    @Bean
    fun postResponseMapper(): PostResponseMapper {
        return PostResponseMapper()
    }
}