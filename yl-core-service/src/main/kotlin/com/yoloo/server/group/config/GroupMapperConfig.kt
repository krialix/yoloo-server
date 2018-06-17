package com.yoloo.server.group.config

import com.yoloo.server.group.mapper.GroupResponseMapper
import com.yoloo.server.group.mapper.SubscriptionUserResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class GroupMapperConfig {

    @Lazy
    @Bean
    fun groupResponseMapper(): GroupResponseMapper {
        return GroupResponseMapper()
    }

    @Lazy
    @Bean
    fun subscriptionUserResponseMapper(): SubscriptionUserResponseMapper {
        return SubscriptionUserResponseMapper()
    }
}