package com.yoloo.server.comment.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.comment.usecase.*
import com.yoloo.server.comment.vo.CommentCollectionResponse
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.CreateCommentRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(
    "/api/posts/{postId}/comments",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class CommentController(
    private val createCommentUseCase: CreateCommentUseCase,
    private val approveCommentUseCase: ApproveCommentUseCase,
    private val disapproveCommentUseCase: DisapproveCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val listCommentsUseCase: ListCommentsUseCase
) {

    @PreAuthorize("hasAnyRole('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: CreateCommentRequest
    ): CommentResponse {
        val user = AuthUtil.from(authentication)

        return createCommentUseCase.execute(user.userId, user.username, user.picture, postId, request)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        deleteCommentUseCase.execute(user.userId, postId, commentId)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun approveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        approveCommentUseCase.execute(user.userId, postId, commentId)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun disapproveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        disapproveCommentUseCase.execute(user.userId, postId, commentId)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping
    fun listComments(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CommentCollectionResponse {
        val user = AuthUtil.from(authentication)

        return listCommentsUseCase.execute(user.userId, postId, cursor)
    }
}