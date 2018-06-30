package com.yoloo.server.comment.config

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.usecase.*
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.service.NotificationQueueService
import org.springframework.beans.factory.annotation.Qualifier
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
        notificationQueueService: NotificationQueueService
    ): CreateCommentUseCase {
        return CreateCommentUseCase(idGenerator, commentResponseMapper, notificationQueueService)
    }

    @Lazy
    @Bean
    fun approveCommentUseCase(notificationQueueService: NotificationQueueService): ApproveCommentUseCase {
        return ApproveCommentUseCase(notificationQueueService)
    }

    @Lazy
    @Bean
    fun disapproveCommentUseCase(): DisapproveCommentUseCase {
        return DisapproveCommentUseCase()
    }

    @Lazy
    @Bean
    fun deleteCommentUseCase(): DeleteCommentUseCase {
        return DeleteCommentUseCase()
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