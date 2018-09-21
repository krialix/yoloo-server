package com.yoloo.server.comment.usecase

import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.CreateCommentRequest
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.vo.Author
import com.yoloo.server.common.vo.Url
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.PostId
import com.yoloo.server.user.entity.User
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory.LongIdGenerator
import org.springframework.stereotype.Service

@Service
class CreateCommentUseCase(
    private val idGenerator: LongIdGenerator,
    private val commentResponseMapper: CommentResponseMapper
) {

    fun execute(
        requesterUserId: Long,
        requesterDisplayName: String,
        requesterAvatarUrl: String,
        postId: Long,
        request: CreateCommentRequest
    ): CommentResponse {
        val userKey = User.createKey(requesterUserId)
        val postKey = Post.createKey(postId)

        val map = ofy().load().keys(userKey, postKey) as Map<*, *>

        val user = map[userKey] as User
        val post = map[postKey] as Post?

        ServiceExceptions.checkNotFound(post != null && !post.isSoftDeleted, PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkForbidden(
            post!!.isCommentingAllowed(), "post.forbidden_commenting"
        )

        val postUser = ofy().load().type(User::class.java).id(post.author.id).now()

        val comment = createComment(post, requesterUserId, requesterDisplayName, requesterAvatarUrl, request)

        post.incCommentCount()
        user.profile.countData.commentCount = user.profile.countData.commentCount.inc()

        ofy().save().entities(comment, post, user)

        addToNotificationQueue(comment, postUser.fcmToken)

        return commentResponseMapper.apply(comment, true, false)
    }

    private fun createComment(
        post: Post,
        requesterUserId: Long,
        requesterDisplayName: String,
        requesterAvatarUrl: String,
        request: CreateCommentRequest
    ): Comment {
        return Comment(
            id = idGenerator.generateId(),
            postId = PostId(post.id, post.author.id),
            author = Author(
                id = requesterUserId,
                displayName = requesterDisplayName,
                profileImageUrl = Url(requesterAvatarUrl)
            ),
            content = CommentContent(request.content!!)
        )
    }

    private fun addToNotificationQueue(comment: Comment, postAuthorFcmToken: String) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_COMMENT))
            .addData("id", comment.id.toString())
            .addData("commentAuthorDisplayName", comment.author.displayName)
            .addData("postId", comment.postId.value.toString())
            .addData("postAuthorFcmToken", postAuthorFcmToken)
            .build()

        notificationQueueService.addQueueAsync(event)*/
    }
}
