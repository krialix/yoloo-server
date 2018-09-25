package com.yoloo.server.post

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.like.service.LikeService
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.usecase.*
import com.yoloo.server.post.vo.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(
    "/api/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostController(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val listBookmarkedFeedUseCase: ListBookmarkedFeedUseCase,
    private val likeService: LikeService,
    private val createCommentUseCase: CreateCommentUseCase,
    private val approveCommentUseCase: ApproveCommentUseCase,
    private val disapproveCommentUseCase: DisapproveCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val listCommentsUseCase: ListCommentsUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/posts/{postId}")
    fun getPost(authentication: Authentication, @PathVariable("postId") postId: String): PostResponse {
        val user = AuthUtil.from(authentication)

        return getPostByIdUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(authentication: Authentication, @RequestBody @Valid request: CreatePostRequest): PostResponse {
        val user = AuthUtil.from(authentication)

        return createPostUseCase.execute(user.userId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PatchMapping("/posts/{postId}")
    fun updatePost(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: UpdatePostRequest
    ) {
        val user = AuthUtil.from(authentication)

        updatePostUseCase.execute(user.userId, postId, request)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        deletePostUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("/posts/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun bookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        bookmarkPostUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/posts/{postId}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unbookmarkPost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        unbookmarkPostUseCase.execute(user.userId, postId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/users/bookmarks")
    fun listBookmarkedPosts(
        authentication: Authentication,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        val user = AuthUtil.from(authentication)

        return listBookmarkedFeedUseCase.execute(user.userId, cursor)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/likes")
    fun likePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        likeService.like(user.userId, postId, Post::class.java)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{postId}/likes")
    fun dislikePost(authentication: Authentication, @PathVariable("postId") postId: Long) {
        val user = AuthUtil.from(authentication)

        likeService.dislike(user.userId, postId, Post::class.java)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestBody @Valid request: CreateCommentRequest
    ): CommentResponse {
        val user = AuthUtil.from(authentication)

        return createCommentUseCase.execute(user.userId, user.username, user.picture, postId, request)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        deleteCommentUseCase.execute(user.userId, postId, commentId)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun approveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        approveCommentUseCase.execute(ApproveCommentUseCase.Input(user.userId, postId, commentId))
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun disapproveComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        disapproveCommentUseCase.execute(user.userId, postId, commentId)
    }

    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping
    fun listComments(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CommentCollectionResponse {
        val user = AuthUtil.from(authentication)

        return listCommentsUseCase.execute(user.userId, postId, cursor)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comments/{commentId}/likes")
    fun voteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        likeService.like(user.userId, commentId, Comment::class.java)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/comments/{commentId}/likes")
    fun unvoteComment(
        authentication: Authentication,
        @PathVariable("postId") postId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        val user = AuthUtil.from(authentication)

        likeService.dislike(user.userId, commentId, Comment::class.java)
    }
}
