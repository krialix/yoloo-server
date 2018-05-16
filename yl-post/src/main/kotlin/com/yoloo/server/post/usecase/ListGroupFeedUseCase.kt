package com.yoloo.server.post.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.response.PostResponse
import com.yoloo.server.post.mapper.PostResponseMapper
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class ListGroupFeedUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) : UseCase<ListGroupFeedUseCase.Request, CollectionResponse<PostResponse>> {

    override fun execute(request: Request): CollectionResponse<PostResponse> {
        var query = ObjectifyProxy.ofy()
            .load()
            .type(Post::class.java)
            .filter("data.group.id", request.topicId)
            .orderKey(true)

        request.cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        val iterator = query.iterator()

        val data = iterator.asSequence()
            .map { postResponseMapper.apply(it) }
            .toList()

        return CollectionResponse.builder<PostResponse>()
            .data(data)
            .prevPageToken(request.cursor)
            .nextPageToken(iterator.cursor.toWebSafeString())
            .build()
    }


    class Request(val principal: Principal?, val topicId: String, val cursor: String?)
}