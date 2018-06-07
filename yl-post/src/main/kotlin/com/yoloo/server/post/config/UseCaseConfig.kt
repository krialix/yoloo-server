package com.yoloo.server.post.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.util.id.LongIdGenerator
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.util.CircularFifoBuffer
import com.yoloo.server.post.vo.GroupInfoResponse
import com.yoloo.server.post.vo.UserInfoResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun approveCommentUseCase(): ApproveCommentUseCase {
        return ApproveCommentUseCase()
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
    fun deleteCommentUseCase(): DeleteCommentUseCase {
        return DeleteCommentUseCase()
    }

    @Lazy
    @Bean
    fun deletePostUseCase(pubSubTemplate: PubSubTemplate): DeletePostUseCase {
        return DeletePostUseCase(pubSubTemplate)
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
        userInfoFetcher: Fetcher<Long, UserInfoResponse>,
        commentResponseMapper: CommentResponseMapper
    ): InsertCommentUseCase {
        return InsertCommentUseCase(idGenerator, userInfoFetcher, commentResponseMapper)
    }

    @Lazy
    @Bean
    fun insertPostUseCase(
        @Qualifier("cached") idGenerator: LongIdGenerator,
        userInfoFetcher: Fetcher<Long, UserInfoResponse>,
        groupInfoFetcher: Fetcher<Long, GroupInfoResponse>,
        pubSubTemplate: PubSubTemplate,
        objectMapper: ObjectMapper,
        postResponseMapper: PostResponseMapper
    ): InsertPostUseCase {
        return InsertPostUseCase(
            idGenerator,
            postResponseMapper,
            userInfoFetcher,
            groupInfoFetcher,
            pubSubTemplate,
            objectMapper
        )
    }

    @Lazy
    @Bean
    fun listBookmarkedPostsUseCase(
        postResponseMapper: PostResponseMapper,
        memcacheService: MemcacheService
    ): ListBookmarkedPostsUseCase {
        return ListBookmarkedPostsUseCase(postResponseMapper, memcacheService)
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