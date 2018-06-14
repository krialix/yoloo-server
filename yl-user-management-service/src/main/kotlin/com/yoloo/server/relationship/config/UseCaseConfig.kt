package com.yoloo.server.relationship.config

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.appengine.service.NotificationService
import com.yoloo.server.relationship.usecase.FollowUseCase
import com.yoloo.server.relationship.usecase.ListRelationshipUseCase
import com.yoloo.server.relationship.usecase.UnfollowUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun followUseCase(memcacheService: AsyncMemcacheService, notificationService: NotificationService): FollowUseCase {
        return FollowUseCase(memcacheService, notificationService)
    }

    @Lazy
    @Bean
    fun unfollowUseCase(memcacheService: AsyncMemcacheService): UnfollowUseCase {
        return UnfollowUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun listRelationshipsUseCase(): ListRelationshipUseCase {
        return ListRelationshipUseCase()
    }
}