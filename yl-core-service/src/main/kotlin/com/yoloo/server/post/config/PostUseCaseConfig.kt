package com.yoloo.server.post.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.taskqueue.Queue
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.config.QueueBeanQualifier
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.util.CircularFifoBuffer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
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
    fun insertPostUseCase(
        @Qualifier(IdBeanQualifier.CACHED) idGenerator: LongIdGenerator,
        postResponseMapper: PostResponseMapper,
        @Qualifier(QueueBeanQualifier.NOTIFICATION) notificationQueue: Queue,
        @Qualifier(QueueBeanQualifier.SEARCH) searchQueue: Queue,
        objectMapper: ObjectMapper
    ): CreatePostUseCase {
        return CreatePostUseCase(idGenerator, postResponseMapper, notificationQueue, searchQueue, objectMapper)
    }

    @Lazy
    @Bean
    fun updatePostUseCase(
        @Qualifier(QueueBeanQualifier.SEARCH) searchQueue: Queue,
        objectMapper: ObjectMapper
    ): UpdatePostUseCase {
        return UpdatePostUseCase(searchQueue, objectMapper)
    }

    @Lazy
    @Bean
    fun deletePostUseCase(
        @Qualifier(QueueBeanQualifier.SEARCH) searchQueue: Queue,
        objectMapper: ObjectMapper
    ): DeletePostUseCase {
        return DeletePostUseCase(searchQueue, objectMapper)
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