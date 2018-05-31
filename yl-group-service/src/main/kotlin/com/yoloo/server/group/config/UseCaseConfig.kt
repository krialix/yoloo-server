package com.yoloo.server.group.config

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.group.mapper.GroupResponseMapper
import com.yoloo.server.group.usecase.GetGroupUseCase
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
}