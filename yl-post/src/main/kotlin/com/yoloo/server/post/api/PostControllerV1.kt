package com.yoloo.server.post.api

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.vo.InsertPostRequest
import com.yoloo.server.post.vo.JwtClaims
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.UpdatePostRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(
    "/api/v1/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostControllerV1(
    private val getPostUseCase: GetPostUseCase,
    private val listGroupFeedUseCase: ListGroupFeedUseCase,
    private val insertPostUseCase: InsertPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val votePostUseCase: VotePostUseCase,
    private val unvotePostUseCase: UnvotePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val listBookmarkedPostsUseCase: ListBookmarkedPostsUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:read')")
    @GetMapping("/{postId}")
    fun getPost(authentication: Authentication, @PathVariable("postId") postId: Long): PostResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return getPostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertPost(authentication: Authentication, @RequestBody @Valid request: InsertPostRequest): PostResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return insertPostUseCase.execute(jwtClaim.sub, request)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:write')")
    @PatchMapping("/{postId}")
    fun updatePost(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: UpdatePostRequest
    ): PostResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return updatePostUseCase.execute(jwtClaim.sub, postId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        deletePostUseCase.execute(jwtClaim.sub, postId)
    }

    @GetMapping("/groups/{groupId}")
    fun listGroupFeed(
        authentication: Authentication,
        @PathVariable("groupId") groupId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val jwtClaim = JwtClaims.from(authentication)

        return listGroupFeedUseCase.execute(jwtClaim.sub, groupId, cursor)
    }

    @PostMapping("/{postId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun votePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        votePostUseCase.execute(jwtClaim.sub, postId)
    }

    @DeleteMapping("/{postId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unvotePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unvotePostUseCase.execute(jwtClaim.sub, postId)
    }

    @PostMapping("/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun bookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        bookmarkPostUseCase.execute(jwtClaim.sub, postId)
    }

    @DeleteMapping("/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unbookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unbookmarkPostUseCase.execute(jwtClaim.sub, postId)
    }

    @GetMapping("/bookmarks")
    fun listBookmarkedPosts(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val jwtClaim = JwtClaims.from(authentication)

        return listBookmarkedPostsUseCase.execute(jwtClaim.sub, cursor)
    }
}