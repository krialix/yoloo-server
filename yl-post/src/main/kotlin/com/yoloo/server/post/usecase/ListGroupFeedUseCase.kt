package com.yoloo.server.post.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class ListGroupFeedUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {
    fun execute(principal: Principal, topicId: String, cursor: String?): CollectionResponse<PostResponse> {
        var query = ofy()
            .load()
            .type(Post::class.java)
            .filter("data.group.id", topicId)
            .orderKey(true)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        val iterator = query.iterator()

        val data = iterator.asSequence()
            .map { postResponseMapper.apply(it, emptyMap()) }
            .toList()

        return CollectionResponse.builder<PostResponse>()
            .data(data)
            .prevPageToken(cursor)
            .nextPageToken(iterator.cursor.toWebSafeString())
            .build()
    }
}