package com.yoloo.server.comment.api

import com.yoloo.server.comment.usecase.ApproveCommentUseCase
import com.yoloo.server.comment.usecase.InsertCommentUseCase
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.InsertCommentRequest
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
    private val approveCommentUseCase: ApproveCommentUseCase
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
    fun approveComment(authentication: Authentication, @PathVariable("commentId") commentId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        approveCommentUseCase.execute(jwtClaim.sub, commentId)
    }
}