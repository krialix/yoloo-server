package com.yoloo.server.group.config

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.group.mapper.GroupResponseMapper
import com.yoloo.server.group.mapper.SubscriptionUserResponseMapper
import com.yoloo.server.group.usecase.GetGroupUseCase
import com.yoloo.server.group.usecase.ListSubscriptionsUseCase
import com.yoloo.server.group.usecase.SubscribeUseCase
import com.yoloo.server.group.usecase.UnsubscribeUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class GroupUseCaseConfig {

    @Lazy
    @Bean
    fun getGroupUseCase(memcacheService: MemcacheService, groupResponseMapper: GroupResponseMapper): GetGroupUseCase {
        return GetGroupUseCase(memcacheService, groupResponseMapper)
    }

    @Lazy
    @Bean
    fun subscribeUseCase(memcacheService: AsyncMemcacheService): SubscribeUseCase {
        return SubscribeUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun unsubscribeUseCase(memcacheService: AsyncMemcacheService): UnsubscribeUseCase {
        return UnsubscribeUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun listSubscriptionsUseCase(mapper: SubscriptionUserResponseMapper): ListSubscriptionsUseCase {
        return ListSubscriptionsUseCase(mapper)
    }
}