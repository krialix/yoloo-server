package com.yoloo.server.comment.usecase

import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.vo.InsertCommentRequest
import com.yoloo.server.comment.vo.PostId
import com.yoloo.server.common.api.exception.ForbiddenException
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.JwtClaims
import com.yoloo.server.post.vo.PostPermFlag
import com.yoloo.server.post.vo.UserInfoResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class InsertCommentUseCase(
    private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<Long, UserInfoResponse>,
    private val eventPublisher: ApplicationEventPublisher,
    private val commentResponseMapper: CommentResponseMapper
) {
    fun execute(jwtClaims: JwtClaims, postId: Long, request: InsertCommentRequest): CommentResponse {
        val userId = jwtClaims.sub

        val post = ofy().load().type(Post::class.java).id(postId).now()

        if (!post.flags.contains(PostPermFlag.ALLOW_COMMENTING)) {
            throw ForbiddenException("post.not_allowed.commenting")
        }

        val userInfoResponse = userInfoFetcher.fetch(userId)

        val comment = Comment(
            id = idGenerator.generateId(),
            postId = PostId(postId),
            author = Author(
                id = userId,
                displayName = userInfoResponse.displayName,
                avatar = AvatarImage(Url(userInfoResponse.image)),
                verified = userInfoResponse.verified
            ),
            content = CommentContent(request.content!!)
        )

        ofy().save().entity(comment)

        return commentResponseMapper.apply(comment)
    }
}