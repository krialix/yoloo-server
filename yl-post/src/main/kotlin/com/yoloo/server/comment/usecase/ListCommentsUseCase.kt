package com.yoloo.server.comment.usecase

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.mapper.CommentResponseMapper
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.common.api.exception.BadRequestException
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.stereotype.Service

@Service
class ListCommentsUseCase(private val commentResponseMapper: CommentResponseMapper) {

    fun execute(requesterId: Long, postId: String, cursor: String?): CollectionResponse<CommentResponse> {
        val post =
            ofy().load().type(Post::class.java).id(postId).now() ?: throw BadRequestException("error.post.not-found")

        var query = ofy().load()
            .type(Comment::class.java)
            .filter("postId.value", postId)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        query = query.limit(50)

        val iterator = query.iterator()

        val data = iterator.asSequence().map { commentResponseMapper.apply(it) }.toList()

        return CollectionResponse.builder<CommentResponse>()
            .data(data)
            .prevPageToken(cursor)
            .nextPageToken(iterator.cursor.toWebSafeString())
            .build()
    }
}