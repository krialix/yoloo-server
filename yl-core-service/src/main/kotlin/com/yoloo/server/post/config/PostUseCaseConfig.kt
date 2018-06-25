package com.yoloo.server.post.config

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.service.NotificationService
import com.yoloo.server.common.queue.service.SearchService
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
        searchService: SearchService,
        notificationService: NotificationService
    ): CreatePostUseCase {
        return CreatePostUseCase(idGenerator, postResponseMapper, searchService, notificationService)
    }

    @Lazy
    @Bean
    fun updatePostUseCase(searchService: SearchService): UpdatePostUseCase {
        return UpdatePostUseCase(searchService)
    }

    @Lazy
    @Bean
    fun deletePostUseCase(searchService: SearchService): DeletePostUseCase {
        return DeletePostUseCase(searchService)
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