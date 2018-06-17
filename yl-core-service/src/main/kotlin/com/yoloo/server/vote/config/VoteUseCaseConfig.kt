package com.yoloo.server.vote.config

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.vote.usecase.UnvoteCommentUseCase
import com.yoloo.server.vote.usecase.UnvotePostUseCase
import com.yoloo.server.vote.usecase.VoteCommentUseCase
import com.yoloo.server.vote.usecase.VotePostUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class VoteUseCaseConfig {

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
    fun voteCommentUseCase(memcacheService: AsyncMemcacheService): VoteCommentUseCase {
        return VoteCommentUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun unvoteCommentUseCase(memcacheService: AsyncMemcacheService): UnvoteCommentUseCase {
        return UnvoteCommentUseCase(memcacheService)
    }
}