package com.yoloo.server.group.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.group.mapper.GroupResponseMapper
import com.yoloo.server.group.usecase.GetGroupUseCase
import com.yoloo.server.group.usecase.SubscribeUseCase
import com.yoloo.server.group.usecase.UnsubscribeUseCase
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun getGroupUseCase(memcacheService: MemcacheService, groupResponseMapper: GroupResponseMapper): GetGroupUseCase {
        return GetGroupUseCase(memcacheService, groupResponseMapper)
    }

    @Lazy
    @Bean
    fun subscribeUseCase(
        memcacheService: MemcacheService,
        pubSubTemplate: PubSubTemplate,
        objectMapper: ObjectMapper
    ): SubscribeUseCase {
        return SubscribeUseCase(memcacheService, pubSubTemplate, objectMapper)
    }

    @Lazy
    @Bean
    fun unsubscribeUseCase(
        memcacheService: MemcacheService,
        pubSubTemplate: PubSubTemplate,
        objectMapper: ObjectMapper
    ): UnsubscribeUseCase {
        return UnsubscribeUseCase(memcacheService, pubSubTemplate, objectMapper)
    }
}