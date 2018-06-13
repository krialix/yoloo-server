package com.yoloo.server.comment.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.BaseUseCase
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.InsertCommentRequest
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.PostId
import com.yoloo.server.post.vo.PostPermFlag
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class InsertCommentUseCase(
    private val idGenerator: LongIdGenerator,
    private val commentResponseMapper: CommentResponseMapper,
    pubSubTemplate: PubSubTemplate,
    objectMapper: ObjectMapper
) : BaseUseCase(objectMapper, pubSubTemplate) {

    fun execute(requester: Requester, postId: Long, request: InsertCommentRequest): CommentResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(
            !post.flags.contains(PostPermFlag.DISABLE_COMMENTING),
            "post.forbidden_commenting"
        )

        val comment = createComment(post, requester, request)

        post.countData.commentCount = post.countData.commentCount.inc()

        val saveFuture = ofy().save().entities(comment, post)
        putResult(saveFuture)

        publishEvent("comment.create", comment)

        return commentResponseMapper.apply(comment, true, false)
    }

    private fun createComment(
        post: Post,
        requester: Requester,
        request: InsertCommentRequest
    ): Comment {
        return Comment(
            id = idGenerator.generateId(),
            postId = PostId(post.id, post.author.id),
            author = Author(
                id = requester.userId,
                displayName = requester.displayName,
                avatar = AvatarImage(Url(requester.avatarUrl)),
                verified = requester.verified
            ),
            content = CommentContent(request.content!!)
        )
    }

    data class Requester(
        val userId: Long,
        val displayName: String,
        val avatarUrl: String,
        val verified: Boolean
    )
}