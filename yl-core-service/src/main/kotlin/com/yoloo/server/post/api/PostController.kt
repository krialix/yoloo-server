package com.yoloo.server.post.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.post.usecase.DeletePostUseCase
import com.yoloo.server.post.usecase.GetPostUseCase
import com.yoloo.server.post.usecase.CreatePostUseCase
import com.yoloo.server.post.usecase.UpdatePostUseCase
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
class PostController(
    private val getPostUseCase: GetPostUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/{postId}")
    fun getPost(authentication: Authentication, @PathVariable("postId") postId: Long): PostResponse {
        val user = AuthUtil.from(authentication)

        return getPostUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertPost(authentication: Authentication, @RequestBody @Valid request: CreatePostRequest): PostResponse {
        val user = AuthUtil.from(authentication)

        return createPostUseCase.execute(user.userId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PatchMapping("/{postId}")
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
    @DeleteMapping("/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        deletePostUseCase.execute(user.userId, postId)
    }
}