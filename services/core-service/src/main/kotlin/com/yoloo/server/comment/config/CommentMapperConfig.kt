package com.yoloo.server.comment.config

import com.yoloo.server.comment.mapper.CommentResponseMapper
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