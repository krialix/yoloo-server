package com.yoloo.server.comment.config

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.usecase.*
import com.yoloo.server.common.id.generator.LongIdGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class CommentUseCaseConfig {

    @Lazy
    @Bean
    fun insertCommentUseCase(
        @Qualifier("cached") idGenerator: LongIdGenerator,
        commentResponseMapper: CommentResponseMapper,
        eventPublisher: ApplicationEventPublisher
    ): InsertCommentUseCase {
        return InsertCommentUseCase(
            idGenerator,
            commentResponseMapper,
            eventPublisher
        )
    }

    @Lazy
    @Bean
    fun approveCommentUseCase(eventPublisher: ApplicationEventPublisher): ApproveCommentUseCase {
        return ApproveCommentUseCase(eventPublisher)
    }

    @Lazy
    @Bean
    fun disapproveCommentUseCase(): DisapproveCommentUseCase {
        return DisapproveCommentUseCase()
    }

    @Lazy
    @Bean
    fun deleteCommentUseCase(eventPublisher: ApplicationEventPublisher): DeleteCommentUseCase {
        return DeleteCommentUseCase(eventPublisher)
    }

    @Lazy
    @Bean
    fun listCommentsUseCase(
        commentResponseMapper: CommentResponseMapper,
        memcacheService: MemcacheService
    ): ListCommentsUseCase {
        return ListCommentsUseCase(commentResponseMapper, memcacheService)
    }
}