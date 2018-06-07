package com.yoloo.server.post.api

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
    "/api/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostController(
    private val getPostUseCase: GetPostUseCase,
    private val insertPostUseCase: InsertPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val votePostUseCase: VotePostUseCase,
    private val unvotePostUseCase: UnvotePostUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/{postId}")
    fun getPost(authentication: Authentication, @PathVariable("postId") postId: Long): PostResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return getPostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertPost(authentication: Authentication, @RequestBody @Valid request: InsertPostRequest): PostResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return insertPostUseCase.execute(jwtClaim.sub, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PatchMapping("/{postId}")
    fun updatePost(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: UpdatePostRequest
    ): PostResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return updatePostUseCase.execute(jwtClaim.sub, postId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        deletePostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("/{postId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun votePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        votePostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/{postId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unvotePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unvotePostUseCase.execute(jwtClaim.sub, postId)
    }

    // TODO Implement search
}