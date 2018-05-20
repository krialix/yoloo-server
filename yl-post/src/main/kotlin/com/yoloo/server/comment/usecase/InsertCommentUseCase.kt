package com.yoloo.server.comment.usecase

import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.InsertCommentRequest
import com.yoloo.server.comment.vo.PostId
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.PostPermFlag
import com.yoloo.server.post.vo.UserInfoResponse
import org.springframework.stereotype.Component

@Component
class InsertCommentUseCase(
    private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<Long, UserInfoResponse>,
    private val commentResponseMapper: CommentResponseMapper
) {
    fun execute(requesterId: Long, request: InsertCommentRequest): CommentResponse {
        val postId = request.postId!!
        var post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not-found")
        ServiceExceptions.checkNotFound(!post.isDeleted(), "post.not-found")
        ServiceExceptions.checkForbidden(
            !post.flags.contains(PostPermFlag.DISABLE_COMMENTING),
            "post.not_allowed.commenting"
        )

        val userResponse = userInfoFetcher.fetch(requesterId)

        val comment = createComment(postId, requesterId, userResponse, request)

        post = updatePost(post)

        val saveResult = ofy().save().entities(comment, post)
        if (AppengineUtil.isTest()) {
            saveResult.now()
        }

        return commentResponseMapper.apply(comment, true, false)
    }

    private fun createComment(
        postId: Long,
        requesterId: Long,
        userResponse: UserInfoResponse,
        request: InsertCommentRequest
    ): Comment {
        return Comment(
            id = idGenerator.generateId(),
            postId = PostId(postId),
            author = Author(
                id = requesterId,
                displayName = userResponse.displayName,
                avatar = AvatarImage(Url(userResponse.image)),
                verified = userResponse.verified
            ),
            content = CommentContent(request.content!!)
        )
    }

    private fun updatePost(post: Post): Post {
        val countData = post.countData.copy(commentCount = post.countData.commentCount.inc())
        return post.copy(countData = countData)
    }
}