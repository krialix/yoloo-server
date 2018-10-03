package com.yoloo.server.post.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.usecase.GetPostByIdUseCase
import com.yoloo.server.post.usecase.ListBookmarkedPostsUseCase
import com.yoloo.server.post.vo.PostResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostQueryController(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val listBookmarkedPostsUseCase: ListBookmarkedPostsUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/posts/{postId}")
    fun getPost(authentication: Authentication, @PathVariable("postId") postId: String): PostResponse {
        val user = AuthUtil.from(authentication)

        return getPostByIdUseCase.execute(GetPostByIdUseCase.Input(user.userId, postId))
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/users/bookmarks")
    fun listBookmarkedPosts(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val user = AuthUtil.from(authentication)

        return listBookmarkedPostsUseCase.execute(user.userId, cursor)
    }
}
