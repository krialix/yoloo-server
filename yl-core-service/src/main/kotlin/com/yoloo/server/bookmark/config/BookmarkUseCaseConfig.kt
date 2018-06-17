package com.yoloo.server.bookmark.config

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.bookmark.usecase.BookmarkPostUseCase
import com.yoloo.server.bookmark.usecase.ListBookmarkedFeedUseCase
import com.yoloo.server.bookmark.usecase.UnbookmarkPostUseCase
import com.yoloo.server.bookmark.usecase.RefreshBookmarkCacheUseCase
import com.yoloo.server.post.mapper.PostResponseMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class BookmarkUseCaseConfig {

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
    fun warmupBookmarkCacheUseCase(memcacheService: AsyncMemcacheService): RefreshBookmarkCacheUseCase {
        return RefreshBookmarkCacheUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun listBookmarkedFeedUseCase(
        postResponseMapper: PostResponseMapper,
        memcacheService: MemcacheService
    ): ListBookmarkedFeedUseCase {
        return ListBookmarkedFeedUseCase(postResponseMapper, memcacheService)
    }
}