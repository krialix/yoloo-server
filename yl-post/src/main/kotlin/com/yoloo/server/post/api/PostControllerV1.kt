package com.yoloo.server.post.api

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.vo.InsertPostRequest
import com.yoloo.server.post.vo.JwtClaims
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.UpdatePostRequest
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
    private val insertPostUseCase: InsertPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val votePostUseCase: VotePostUseCase,
    private val unvotePostUseCase: UnvotePostUseCase
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

        return insertPostUseCase.execute(jwtClaim.sub, request)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:write')")
    @PatchMapping("/{postId}")
    fun updatePost(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: UpdatePostRequest
    ): PostResponse {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return updatePostUseCase.execute(jwtClaim.sub, postId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER') or #oauth2.hasScope('post:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        deletePostUseCase.execute(jwtClaim.sub, postId)
    }

    @GetMapping("/groups/{groupId}")
    fun listGroupPosts(
        authentication: Authentication,
        @PathVariable("groupId") groupId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        return listGroupFeedUseCase.execute(jwtClaim.sub, groupId, cursor)
    }

    @PutMapping("/{postId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun votePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        votePostUseCase.execute(jwtClaim.sub, postId)
    }

    @DeleteMapping("/{postId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unvotePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims

        unvotePostUseCase.execute(jwtClaim.sub, postId)
    }

    @PutMapping("/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun bookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims


    }

    @DeleteMapping("/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unbookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val details = authentication.details as OAuth2AuthenticationDetails
        val jwtClaim = details.decodedDetails as JwtClaims


    }
}