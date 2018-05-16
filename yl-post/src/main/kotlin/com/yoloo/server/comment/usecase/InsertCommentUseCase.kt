package com.yoloo.server.comment.usecase

import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.requestpayload.InsertCommentPayload
import com.yoloo.server.comment.response.CommentResponse
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.PostId
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.common.api.exception.ForbiddenException
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.response.UserInfoResponse
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.PostAcl
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class InsertCommentUseCase(
    private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<Long, UserInfoResponse>,
    private val eventPublisher: ApplicationEventPublisher,
    private val commentResponseMapper: CommentResponseMapper
) : UseCase<InsertCommentUseCase.Request, CommentResponse> {

    override fun execute(request: Request): CommentResponse {
        val userId = request.principal.name.toLong()
        val postId = request.postId

        val post = ofy().load().type(Post::class.java).id(postId).now()

        if (!post.acls.contains(PostAcl.ALLOW_COMMENTING)) {
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
            content = CommentContent(request.payload.content!!)
        )

        ofy().save().entity(comment)

        return commentResponseMapper.apply(comment)
    }

    class Request(val principal: Principal, val postId: Long, val payload: InsertCommentPayload)
}