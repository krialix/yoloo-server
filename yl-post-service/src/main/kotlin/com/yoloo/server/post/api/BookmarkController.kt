package com.yoloo.server.post.api

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.usecase.BookmarkPostUseCase
import com.yoloo.server.post.usecase.ListBookmarkedPostsUseCase
import com.yoloo.server.post.usecase.UnbookmarkPostUseCase
import com.yoloo.server.post.usecase.WarmUpBookmarkCacheUseCase
import com.yoloo.server.post.vo.JwtClaims
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
    private val warmUpBookmarkCacheUseCase: WarmUpBookmarkCacheUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val listBookmarkedPostsUseCase: ListBookmarkedPostsUseCase
) {

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/bookmarks/cache/warmup")
    @ResponseStatus(HttpStatus.CREATED)
    fun warmupCache() {
        warmUpBookmarkCacheUseCase.execute()
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("/posts/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun bookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        bookmarkPostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/posts/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unbookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unbookmarkPostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/users/bookmarks")
    fun listBookmarkedPosts(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val jwtClaim = JwtClaims.from(authentication)

        return listBookmarkedPostsUseCase.execute(jwtClaim.sub, cursor)
    }
}