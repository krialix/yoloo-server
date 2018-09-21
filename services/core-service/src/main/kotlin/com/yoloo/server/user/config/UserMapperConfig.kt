package com.yoloo.server.user.config

import com.yoloo.server.user.mapper.UserResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UserMapperConfig {

    @Lazy
    @Bean
    fun userResponseMapper(): UserResponseMapper {
        return UserResponseMapper()
    }
}