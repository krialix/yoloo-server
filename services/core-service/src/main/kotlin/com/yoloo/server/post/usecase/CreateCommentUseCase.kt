package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.vo.Author
import com.yoloo.server.filter.FilterService
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.vo.CommentContent
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.post.vo.CreateCommentRequest
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.user.entity.User
import com.yoloo.spring.autoconfiguration.appengine.services.counter.CounterService
import com.yoloo.spring.autoconfiguration.appengine.services.notification.NotificationService
import com.yoloo.spring.autoconfiguration.appengine.services.notification.Payload
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory.LongIdGenerator
import org.springframework.stereotype.Service

@Service
class CreateCommentUseCase(
    private val hashIds: Hashids,
    private val filterService: FilterService,
    private val counterService: CounterService,
    private val notificationService: NotificationService,
    private val idGenerator: LongIdGenerator,
    private val commentResponseMapper: CommentResponseMapper
) : AbstractUseCase<CreateCommentUseCase.Input, CommentResponse>() {

    override fun onExecute(input: Input): CommentResponse {
        val hashedPostId = hashIds.decode(input.postId)
        val postId = hashedPostId[0]
        val postAuthorId = hashedPostId[1]

        val entityCache = filterService.get()

        //checkException(entityCache.contains(postId), Status.NOT_FOUND, PostErrors.NOT_FOUND)

        val postKey = Post.createKey(postId)
        val postAuthorKey = User.createKey(postAuthorId)
        val userKey = User.createKey(input.requesterUserId)

        val map = ofy().load().keys(userKey, postKey, postAuthorKey) as Map<*, *>

        val user = map[userKey] as User
        val post = map[postKey] as Post
        val postAuthor = map[postAuthorKey] as User

        val comment = createComment(post, user, input.request)

        ofy().defer().save().entity(comment)

        entityCache.add(comment.id)
        filterService.saveAsync(entityCache)

        counterService.increment("POST_COMMENT:${post.id}", "USER_COMMENT:${user.id}")

        notificationService.addAsync(
            Payload.newBuilder("NEW_COMMENT")
                .addData("id", comment.id.toString())
                .addData("commentAuthorDisplayName", comment.author.displayName)
                .addData("postId", comment.postId.toString())
                .addData("postAuthorFcmToken", postAuthor.fcmToken)
                .build()
        )

        return commentResponseMapper.apply(comment)
    }

    private fun createComment(
        post: Post,
        user: User,
        request: CreateCommentRequest
    ): Comment {
        return Comment(
            id = idGenerator.generateId(),
            postId = post.id,
            author = Author(
                id = user.id,
                displayName = user.profile.displayName.value,
                profileImageUrl = user.profile.profileImageUrl
            ),
            content = CommentContent(request.content!!)
        )
    }

    data class Input(val requesterUserId: Long, val postId: String, val request: CreateCommentRequest)
}
