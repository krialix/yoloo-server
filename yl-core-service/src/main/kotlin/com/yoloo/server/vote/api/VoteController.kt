package com.yoloo.server.vote.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.vote.usecase.UnvoteUseCase
import com.yoloo.server.vote.usecase.VoteUseCase
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
@PreAuthorize("hasAuthority('MEMBER')")
class VoteController(
    private val voteUseCase: VoteUseCase,
    private val unvoteUseCase: UnvoteUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/votes")
    fun votePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        voteUseCase.execute(user.userId, postId, Post::class.java)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{postId}/votes")
    fun unvotePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        unvoteUseCase.execute(user.userId, postId, Post::class.java)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comments/{commentId}/votes")
    fun voteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        voteUseCase.execute(user.userId, commentId, Comment::class.java)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/comments/{commentId}/votes")
    fun unvoteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        unvoteUseCase.execute(user.userId, commentId, Comment::class.java)
    }
}
