package com.yoloo.server.post.config

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.service.NotificationQueueService
import com.yoloo.server.common.queue.service.SearchQueueService
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.util.CircularFifoBuffer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class PostUseCaseConfig {

    @Lazy
    @Bean
    fun getPostUseCase(postResponseMapper: PostResponseMapper, memcacheService: MemcacheService): GetPostUseCase {
        return GetPostUseCase(postResponseMapper, memcacheService)
    }

    @Lazy
    @Bean
    fun createPostUseCase(
        @Qualifier(IdBeanQualifier.CACHED) idGenerator: LongIdGenerator,
        postResponseMapper: PostResponseMapper,
        searchQueueService: SearchQueueService,
        notificationQueueService: NotificationQueueService
    ): CreatePostUseCase {
        return CreatePostUseCase(idGenerator, postResponseMapper, searchQueueService, notificationQueueService)
    }

    @Lazy
    @Bean
    fun updatePostUseCase(searchQueueService: SearchQueueService): UpdatePostUseCase {
        return UpdatePostUseCase(searchQueueService)
    }

    @Lazy
    @Bean
    fun deletePostUseCase(searchQueueService: SearchQueueService): DeletePostUseCase {
        return DeletePostUseCase(searchQueueService)
    }

    @Lazy
    @Bean
    fun listBountyFeedUseCase(
        postResponseMapper: PostResponseMapper,
        memcacheService: MemcacheService
    ): ListBountyFeedUseCase {
        return ListBountyFeedUseCase(postResponseMapper, memcacheService)
    }

    @Lazy
    @Bean
    fun listGroupFeedUseCase(
        postResponseMapper: PostResponseMapper,
        memcacheService: MemcacheService
    ): ListGroupFeedUseCase {
        return ListGroupFeedUseCase(postResponseMapper, memcacheService)
    }

    @Lazy
    @Bean
    fun listAnonymousFeedUseCase(postResponseMapper: PostResponseMapper): ListAnonymousMainFeedUseCase {
        val holder = CircularFifoBuffer(100)
        return ListAnonymousMainFeedUseCase(holder, postResponseMapper)
    }
}