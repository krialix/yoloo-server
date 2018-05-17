package com.yoloo.server.post.api

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.post.usecase.GetPostUseCase
import com.yoloo.server.post.usecase.InsertPostUseCase
import com.yoloo.server.post.usecase.ListGroupFeedUseCase
import com.yoloo.server.post.vo.InsertPostRequest
import com.yoloo.server.post.vo.JwtClaims
import com.yoloo.server.post.vo.PostResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(
    "/api/v1/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostControllerV1(
    private val getPostUseCase: GetPostUseCase,
    private val listGroupFeedUseCase: ListGroupFeedUseCase,
    private val insertPostUseCase: InsertPostUseCase
) {
    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:read')")
    @GetMapping("/{postId}")
    fun getPost(authentication: Authentication, @PathVariable("postId") postId: Long): PostResponse {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return getPostUseCase.execute(jwtClaim.sub, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertPost(authentication: Authentication, @RequestBody @Valid request: InsertPostRequest): PostResponse {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return insertPostUseCase.execute(jwtClaim, request)
    }

    @GetMapping("/topics/{id}")
    fun listTopicPosts(
        authentication: Authentication,
        @PathVariable("id") topicId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return listGroupFeedUseCase.execute(jwtClaim.sub, topicId, cursor)
    }
}