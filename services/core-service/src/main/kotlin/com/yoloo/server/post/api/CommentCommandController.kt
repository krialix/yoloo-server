package com.yoloo.server.post.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.like.service.LikeService
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.usecase.ApproveCommentUseCase
import com.yoloo.server.post.usecase.CreateCommentUseCase
import com.yoloo.server.post.usecase.DeleteCommentUseCase
import com.yoloo.server.post.usecase.DisapproveCommentUseCase
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.post.vo.CreateCommentRequest
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
class CommentCommandController(
    private val likeService: LikeService,
    private val createCommentUseCase: CreateCommentUseCase,
    private val approveCommentUseCase: ApproveCommentUseCase,
    private val disapproveCommentUseCase: DisapproveCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) {

    @PreAuthorize("hasAnyRole('MEMBER')")
    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        authentication: Authentication,
        @PathVariable("postId") postId: String,
        @RequestBody @Valid request: CreateCommentRequest
    ): CommentResponse {
        val user = AuthUtil.from(authentication)

        return createCommentUseCase.execute(CreateCommentUseCase.Input(user.userId, postId, request))
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: String,
        @PathVariable("commentId") commentId: String
    ) {
        val user = AuthUtil.from(authentication)

        deleteCommentUseCase.execute(DeleteCommentUseCase.Input(user.userId, postId, commentId))
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{postId}/comments/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun approveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        approveCommentUseCase.execute(ApproveCommentUseCase.Input(user.userId, postId, commentId))
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{postId}/comments/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun disapproveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        disapproveCommentUseCase.execute(user.userId, postId, commentId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{postId}/comments/{commentId}/like")
    fun likeComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        likeService.like(user.userId, commentId, Comment::class.java)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}/comments/{commentId}/like")
    fun dislikeComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        likeService.dislike(user.userId, commentId, Comment::class.java)
    }
}
