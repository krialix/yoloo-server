package com.yoloo.server.post.config

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.id.config.IdGenQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
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
        @Qualifier(IdGenQualifier.CACHED) idGenerator: LongIdGenerator,
        eventPublisher: ApplicationEventPublisher,
        postResponseMapper: PostResponseMapper
    ): InsertPostUseCase {
        return InsertPostUseCase(idGenerator, postResponseMapper, eventPublisher)
    }

    @Lazy
    @Bean
    fun updatePostUseCase(eventPublisher: ApplicationEventPublisher): UpdatePostUseCase {
        return UpdatePostUseCase(eventPublisher)
    }

    @Lazy
    @Bean
    fun deletePostUseCase(eventPublisher: ApplicationEventPublisher): DeletePostUseCase {
        return DeletePostUseCase(eventPublisher)
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