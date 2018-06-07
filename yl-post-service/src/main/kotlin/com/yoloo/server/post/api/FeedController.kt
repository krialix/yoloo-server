package com.yoloo.server.post.api

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.usecase.ListAnonymousMainFeedUseCase
import com.yoloo.server.post.usecase.ListGroupFeedUseCase
import com.yoloo.server.post.vo.JwtClaims
import com.yoloo.server.post.vo.PostResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api/feeds",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class FeedController(
    private val listAnonymousMainFeedUseCase: ListAnonymousMainFeedUseCase,
    private val listGroupFeedUseCase: ListGroupFeedUseCase
) {

    @PreAuthorize("hasAuthority('ANONYMOUS')")
    @GetMapping("/anonymous")
    fun getPost(authentication: Authentication): CollectionResponse<PostResponse> {
        return listAnonymousMainFeedUseCase.execute()
    }

    @GetMapping("/groups/{groupId}")
    fun listGroupFeed(
        authentication: Authentication,
        @PathVariable("groupId") groupId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val jwtClaim = JwtClaims.from(authentication)

        return listGroupFeedUseCase.execute(jwtClaim.sub, groupId, cursor)
    }
}