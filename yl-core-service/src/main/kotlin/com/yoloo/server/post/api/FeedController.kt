package com.yoloo.server.post.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.usecase.ListAnonymousMainFeedUseCase
import com.yoloo.server.post.usecase.ListBountyFeedUseCase
import com.yoloo.server.post.usecase.ListGroupFeedUseCase
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
    private val listGroupFeedUseCase: ListGroupFeedUseCase,
    private val listBountyFeedUseCase: ListBountyFeedUseCase
) {

    @PreAuthorize("hasAnyAuthority('ANONYMOUS', 'MEMBER')")
    @GetMapping("/anonymous")
    fun getAnonymousFeed(authentication: Authentication): CollectionResponse<PostResponse> {
        return listAnonymousMainFeedUseCase.execute()
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/groups/{groupId}")
    fun listGroupFeed(
        authentication: Authentication,
        @PathVariable("groupId") groupId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val user = AuthUtil.from(authentication)

        return listGroupFeedUseCase.execute(user.userId, groupId, cursor)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/bounty")
    fun listBountyFeed(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val user = AuthUtil.from(authentication)

        return listBountyFeedUseCase.execute(user.userId, cursor)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/users")
    fun listUserFeed(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<*> {

        return CollectionResponse.builder<Any>().build()
    }
}