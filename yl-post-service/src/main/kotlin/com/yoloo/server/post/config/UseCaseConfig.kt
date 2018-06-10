package com.yoloo.server.post.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.post.fetcher.GroupInfoFetcher
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.util.CircularFifoBuffer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun approveCommentUseCase(objectMapper: ObjectMapper, pubSubTemplate: PubSubTemplate): ApproveCommentUseCase {
        return ApproveCommentUseCase(objectMapper, pubSubTemplate)
    }

    @Lazy
    @Bean
    fun disapproveCommentUseCase(): DisapproveCommentUseCase {
        return DisapproveCommentUseCase()
    }

    @Lazy
    @Bean
    fun bookmarkPostUseCase(memcacheService: AsyncMemcacheService): BookmarkPostUseCase {
        return BookmarkPostUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun unbookmarkedPostUseCase(memcacheService: AsyncMemcacheService): UnbookmarkPostUseCase {
        return UnbookmarkPostUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun deleteCommentUseCase(pubSubTemplate: PubSubTemplate, objectMapper: ObjectMapper): DeleteCommentUseCase {
        return DeleteCommentUseCase(pubSubTemplate, objectMapper)
    }

    @Lazy
    @Bean
    fun deletePostUseCase(pubSubTemplate: PubSubTemplate, objectMapper: ObjectMapper): DeletePostUseCase {
        return DeletePostUseCase(pubSubTemplate, objectMapper)
    }

    @Lazy
    @Bean
    fun getPostUseCase(postResponseMapper: PostResponseMapper, memcacheService: MemcacheService): GetPostUseCase {
        return GetPostUseCase(postResponseMapper, memcacheService)
    }

    @Lazy
    @Bean
    fun insertCommentUseCase(
        @Qualifier("cached") idGenerator: LongIdGenerator,
        commentResponseMapper: CommentResponseMapper,
        pubSubTemplate: PubSubTemplate,
        objectMapper: ObjectMapper
    ): InsertCommentUseCase {
        return InsertCommentUseCase(idGenerator, commentResponseMapper, pubSubTemplate, objectMapper)
    }

    @Lazy
    @Bean
    fun insertPostUseCase(
        @Qualifier("cached") idGenerator: LongIdGenerator,
        groupInfoFetcher: GroupInfoFetcher,
        pubSubTemplate: PubSubTemplate,
        objectMapper: ObjectMapper,
        postResponseMapper: PostResponseMapper
    ): InsertPostUseCase {
        return InsertPostUseCase(
            idGenerator,
            postResponseMapper,
            groupInfoFetcher,
            pubSubTemplate,
            objectMapper
        )
    }

    @Lazy
    @Bean
    fun listBookmarkedFeedUseCase(
        postResponseMapper: PostResponseMapper,
        memcacheService: MemcacheService
    ): ListBookmarkedFeedUseCase {
        return ListBookmarkedFeedUseCase(postResponseMapper, memcacheService)
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
    fun listCommentsUseCase(
        commentResponseMapper: CommentResponseMapper,
        memcacheService: MemcacheService
    ): ListCommentsUseCase {
        return ListCommentsUseCase(commentResponseMapper, memcacheService)
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
    fun voteCommentUseCase(memcacheService: AsyncMemcacheService): VoteCommentUseCase {
        return VoteCommentUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun unvoteCommentUseCase(memcacheService: AsyncMemcacheService): UnvoteCommentUseCase {
        return UnvoteCommentUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun votePostUseCase(memcacheService: AsyncMemcacheService): VotePostUseCase {
        return VotePostUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun unvotePostUseCase(memcacheService: AsyncMemcacheService): UnvotePostUseCase {
        return UnvotePostUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun updatePostUseCase(
        postResponseMapper: PostResponseMapper,
        memcacheService: MemcacheService,
        objectMapper: ObjectMapper,
        pubSubTemplate: PubSubTemplate
    ): UpdatePostUseCase {
        return UpdatePostUseCase(postResponseMapper, memcacheService, objectMapper, pubSubTemplate)
    }

    @Lazy
    @Bean
    fun warmupBookmarkCacheUseCase(memcacheService: AsyncMemcacheService): WarmUpBookmarkCacheUseCase {
        return WarmUpBookmarkCacheUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun listAnonymousFeedUseCase(postResponseMapper: PostResponseMapper): ListAnonymousMainFeedUseCase {
        val holder = CircularFifoBuffer(100)
        return ListAnonymousMainFeedUseCase(holder, postResponseMapper)
    }
}