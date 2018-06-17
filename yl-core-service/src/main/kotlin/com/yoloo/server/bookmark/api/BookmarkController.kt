package com.yoloo.server.bookmark.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.bookmark.usecase.BookmarkPostUseCase
import com.yoloo.server.bookmark.usecase.ListBookmarkedFeedUseCase
import com.yoloo.server.bookmark.usecase.RefreshBookmarkCacheUseCase
import com.yoloo.server.bookmark.usecase.UnbookmarkPostUseCase
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.vo.PostResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class BookmarkController(
    private val refreshBookmarkCacheUseCase: RefreshBookmarkCacheUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val listBookmarkedFeedUseCase: ListBookmarkedFeedUseCase
) {

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/bookmarks/cache/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    fun refreshCache() {
        refreshBookmarkCacheUseCase.execute()
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("/posts/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun bookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        bookmarkPostUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/posts/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unbookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        unbookmarkPostUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/users/bookmarks")
    fun listBookmarkedPosts(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val user = AuthUtil.from(authentication)

        return listBookmarkedFeedUseCase.execute(user.userId, cursor)
    }
}