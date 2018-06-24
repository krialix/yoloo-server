package com.yoloo.server.comment.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.taskqueue.Queue
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.usecase.*
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.config.QueueBeanQualifier
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
        @Qualifier(IdBeanQualifier.CACHED) idGenerator: LongIdGenerator,
        commentResponseMapper: CommentResponseMapper,
        @Qualifier(QueueBeanQualifier.NOTIFICATION) notificationQueue: Queue,
        objectMapper: ObjectMapper
    ): CreateCommentUseCase {
        return CreateCommentUseCase(
            idGenerator,
            commentResponseMapper,
            notificationQueue,
            objectMapper
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