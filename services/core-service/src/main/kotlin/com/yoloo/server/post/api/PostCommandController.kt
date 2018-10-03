package com.yoloo.server.post.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.like.service.LikeService
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.vo.CreatePostRequest
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.UpdatePostRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(
    "/api/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostCommandController(
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val likeService: LikeService
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(authentication: Authentication, @RequestBody @Valid request: CreatePostRequest): PostResponse {
        val user = AuthUtil.from(authentication)

        return createPostUseCase.execute(user.userId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PatchMapping("/posts/{postId}")
    fun updatePost(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: UpdatePostRequest
    ) {
        val user = AuthUtil.from(authentication)

        updatePostUseCase.execute(user.userId, postId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        deletePostUseCase.execute(user.userId, postId)
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/likes")
    fun likePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        likeService.like(user.userId, postId, Post::class.java)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{postId}/likes")
    fun dislikePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        likeService.dislike(user.userId, postId, Post::class.java)
    }
}
