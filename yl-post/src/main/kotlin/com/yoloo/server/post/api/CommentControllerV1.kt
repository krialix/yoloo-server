package com.yoloo.server.post.api

import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.vo.CommentCollectionResponse
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.post.vo.InsertCommentRequest
import com.yoloo.server.post.vo.JwtClaims
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(
    "/api/v1/comments",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class CommentControllerV1(
    private val insertCommentUseCase: InsertCommentUseCase,
    private val approveCommentUseCase: ApproveCommentUseCase,
    private val disapproveCommentUseCase: DisapproveCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val voteCommentUseCase: VoteCommentUseCase,
    private val unvoteCommentUseCase: UnvoteCommentUseCase,
    private val listCommentsUseCase: ListCommentsUseCase
) {
    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertComment(authentication: Authentication, @RequestBody @Valid request: InsertCommentRequest): CommentResponse {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return insertCommentUseCase.execute(jwtClaim.sub, request)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:write')")
    @PatchMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun approveComment(authentication: Authentication, @PathVariable("commentId") commentId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        approveCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:write')")
    @PatchMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun disapproveComment(authentication: Authentication, @PathVariable("commentId") commentId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        disapproveCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:write')")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(authentication: Authentication, @PathVariable("commentId") commentId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        deleteCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:write')")
    @PutMapping("/{commentId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun voteComment(authentication: Authentication, @PathVariable("commentId") commentId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        voteCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:write')")
    @DeleteMapping("/{commentId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unvoteComment(authentication: Authentication, @PathVariable("commentId") commentId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        unvoteCommentUseCase.execute(jwtClaim.sub, commentId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('comment:read')")
    @GetMapping
    fun listComments(
        authentication: Authentication,
        @RequestParam("postId") postId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CommentCollectionResponse {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return listCommentsUseCase.execute(jwtClaim.sub, postId, cursor)
    }
}