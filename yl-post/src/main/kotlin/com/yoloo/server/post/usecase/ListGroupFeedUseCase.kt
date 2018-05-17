package com.yoloo.server.post.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.util.Filters
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class ListGroupFeedUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {
    fun execute(requesterId: Long, groupId: String, cursor: String?): CollectionResponse<PostResponse> {
        var query = ofy()
            .load()
            .type(Post::class.java)
            .filter("data.group.id", groupId)
            .orderKey(true)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        val keys = query.keys().list()

        val subscriptionFilter = memcacheService.get(Filters.KEY_FILTER_SUBSCRIPTION) as NanoCuckooFilter

        return ofy()
            .load()
            .keys(keys)
            .values
            .asSequence()
            .map { postResponseMapper.apply(it, subscriptionFilter.contains("$requesterId:$groupId")) }
            .toList()
            .let {
                CollectionResponse.builder<PostResponse>()
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(Cursor.fromWebSafeString(keys.last().toWebSafeString()).toWebSafeString())
                    .build()
            }
    }
}