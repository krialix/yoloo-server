package com.yoloo.server.comment.usecase

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.common.api.exception.BadRequestException
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class ListCommentsUseCase(
    private val commentResponseMapper: CommentResponseMapper
) : UseCase<ListCommentsUseCase.Request, CollectionResponse<CommentResponse>> {

    override fun execute(request: Request): CollectionResponse<CommentResponse> {
        val postId = request.postId

        val post =
            ofy().load().type(Post::class.java).id(postId).now() ?: throw BadRequestException("error.post.not-found")

        var query = ofy().load()
            .type(Comment::class.java)
            .filter("postId.value", postId)

        request.cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        query = query.limit(50)

        val iterator = query.iterator()

        val data = iterator.asSequence().map { commentResponseMapper.apply(it) }.toList()

        return CollectionResponse.builder<CommentResponse>()
            .data(data)
            .prevPageToken(request.cursor)
            .nextPageToken(iterator.cursor.toWebSafeString())
            .build()
    }

    class Request(val principal: Principal?, val postId: String, val cursor: String?)
}