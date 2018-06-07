package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.util.AppengineUtil
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.util.id.LongIdGenerator
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.vo.*
import com.yoloo.server.rest.exception.ServiceExceptions
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class InsertCommentUseCase(
    private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<Long, UserInfoResponse>,
    private val commentResponseMapper: CommentResponseMapper,
    private val pubSubTemplate: PubSubTemplate,
    private val objectMapper: ObjectMapper
) {

    fun execute(requesterId: Long, request: InsertCommentRequest): CommentResponse {
        val postId = request.postId!!
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(
            !post.flags.contains(PostPermFlag.DISABLE_COMMENTING),
            "post.forbidden_commenting"
        )

        val userResponse = userInfoFetcher.fetch(requesterId)

        val comment = createComment(postId, requesterId, userResponse, request)

        post.countData.commentCount.inc()

        val saveResult = ofy().save().entities(comment, post)
        if (AppengineUtil.isTest()) {
            saveResult.now()
        }

        publishCommentCreatedEvent(comment)

        return commentResponseMapper.apply(comment, true, false)
    }

    private fun publishCommentCreatedEvent(comment: Comment) {
        val json = objectMapper.writeValueAsString(comment)
        pubSubTemplate.publish("comment.create", json, null)
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
}