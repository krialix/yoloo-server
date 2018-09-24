package com.yoloo.server.post.config

import com.yoloo.server.post.mapper.CommentResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class CommentMapperConfig {

    @Lazy
    @Bean
    fun commentResponseMapper(): CommentResponseMapper {
        return CommentResponseMapper()
    }
}
