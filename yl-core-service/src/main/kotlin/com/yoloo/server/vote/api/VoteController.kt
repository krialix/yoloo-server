package com.yoloo.server.vote.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.vote.usecase.UnvoteCommentUseCase
import com.yoloo.server.vote.usecase.UnvotePostUseCase
import com.yoloo.server.vote.usecase.VoteCommentUseCase
import com.yoloo.server.vote.usecase.VotePostUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api/votes",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
@ResponseStatus(HttpStatus.NO_CONTENT)
@PreAuthorize("hasAuthority('MEMBER')")
class VoteController(
    private val votePostUseCase: VotePostUseCase,
    private val unvotePostUseCase: UnvotePostUseCase,
    private val voteCommentUseCase: VoteCommentUseCase,
    private val unvoteCommentUseCase: UnvoteCommentUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/votes")
    fun votePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        votePostUseCase.execute(user.userId, postId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{postId}/votes")
    fun unvotePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        unvotePostUseCase.execute(user.userId, postId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comments/{commentId}/votes")
    fun voteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        voteCommentUseCase.execute(user.userId, commentId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/comments/{commentId}/votes")
    fun unvoteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        unvoteCommentUseCase.execute(user.userId, commentId)
    }
}