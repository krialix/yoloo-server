package com.yoloo.server.comment.api

import com.yoloo.server.comment.usecase.*
import com.yoloo.server.comment.vo.CommentCollectionResponse
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.InsertCommentRequest
import com.yoloo.server.post.vo.JwtClaims
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
    private val insertCommentUseCase: InsertCommentUseCase,
    private val approveCommentUseCase: ApproveCommentUseCase,
    private val disapproveCommentUseCase: DisapproveCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val voteCommentUseCase: VoteCommentUseCase,
    private val unvoteCommentUseCase: UnvoteCommentUseCase,
    private val listCommentsUseCase: ListCommentsUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: InsertCommentRequest
    ): CommentResponse {
        val jwtClaim = JwtClaims.from(authentication)

        val requester = InsertCommentUseCase.Requester(jwtClaim.sub, jwtClaim.displayName, jwtClaim.picture, false)

        return insertCommentUseCase.execute(requester, postId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val jwtClaim = JwtClaims.from(authentication)

        deleteCommentUseCase.execute(jwtClaim.sub, postId, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PatchMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun approveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val jwtClaim = JwtClaims.from(authentication)

        approveCommentUseCase.execute(jwtClaim.sub, postId, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun disapproveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val jwtClaim = JwtClaims.from(authentication)

        disapproveCommentUseCase.execute(jwtClaim.sub, postId, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PutMapping("/{commentId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun voteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val jwtClaim = JwtClaims.from(authentication)

        voteCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/{commentId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unvoteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val jwtClaim = JwtClaims.from(authentication)

        unvoteCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping
    fun listComments(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CommentCollectionResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return listCommentsUseCase.execute(jwtClaim.sub, postId, cursor)
    }
}