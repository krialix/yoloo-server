package com.yoloo.server.user.infrastructure

import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserServiceAutoConfiguration {

    @Bean
    fun userResponseMapper(): UserResponseMapper {
        return UserResponseMapper()
    }
}