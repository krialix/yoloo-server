package com.yoloo.server.comment.domain.usecase.impl

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.comment.domain.entity.Comment
import com.yoloo.server.comment.domain.response.CommentResponse
import com.yoloo.server.comment.domain.usecase.ListCommentsUseCase
import com.yoloo.server.comment.domain.usecase.contract.ListCommentsContract
import com.yoloo.server.comment.infrastructure.mapper.CommentResponseMapper
import com.yoloo.server.common.api.exception.BadRequestException
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import org.springframework.stereotype.Service

@Service
class ListCommentsUseCaseImpl(private val commentResponseMapper: CommentResponseMapper) : ListCommentsUseCase {

    override fun execute(request: ListCommentsContract.Request): ListCommentsContract.Response {
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

        val response = CollectionResponse.builder<CommentResponse>()
            .data(data)
            .prevPageToken(request.cursor)
            .nextPageToken(iterator.cursor.toWebSafeString())
            .build()

        return ListCommentsContract.Response(response)
    }
}