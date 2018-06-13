package com.yoloo.server.group.config

import com.yoloo.server.group.mapper.GroupResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class MapperConfig {

    @Lazy
    @Bean
    fun groupResponseMapper(): GroupResponseMapper {
        return GroupResponseMapper()
    }
}