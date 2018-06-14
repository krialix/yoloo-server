package com.yoloo.server.admin.config

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.admin.usecase.DeleteUserUseCase
import com.yoloo.server.admin.usecase.WarmUpRelationshipCacheUseCase
import com.yoloo.server.admin.usecase.WarmUpUserIdentifierCacheUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun deleteUserUseCase(): DeleteUserUseCase {
        return DeleteUserUseCase()
    }

    @Lazy
    @Bean
    fun warmupUserIdentifierCacheUseCase(memcacheService: AsyncMemcacheService): WarmUpUserIdentifierCacheUseCase {
        return WarmUpUserIdentifierCacheUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun warmupRelationshipCacheUseCase(memcacheService: AsyncMemcacheService): WarmUpRelationshipCacheUseCase {
        return WarmUpRelationshipCacheUseCase(memcacheService)
    }
}