package com.yoloo.server.post.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.usecase.ListCommentsUseCase
import com.yoloo.server.post.vo.CommentResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class CommentQueryController(private val listCommentsUseCase: ListCommentsUseCase) {

    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/{postId}/comments")
    fun listComments(
        authentication: Authentication,
        @PathVariable("postId") postId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<CommentResponse> {
        val user = AuthUtil.from(authentication)

        return listCommentsUseCase.execute(ListCommentsUseCase.Input(user.userId, postId, cursor))
    }
}
