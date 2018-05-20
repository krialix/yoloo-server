package com.yoloo.server.post.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.util.Filters
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class ListGroupFeedUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {
    fun execute(requesterId: Long, groupId: Long, cursor: String?): CollectionResponse<PostResponse> {
        var query = ofy()
            .load()
            .type(Post::class.java)
            .filter("group.id", groupId)
            .orderKey(true)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        val queryResultIterator = query.iterator()

        if (!queryResultIterator.hasNext()) {
            return CollectionResponse.builder<PostResponse>().data(emptyList()).build()
        }

        val voteFilter = memcacheService.get(Filters.KEY_FILTER_VOTE) as NanoCuckooFilter

        return queryResultIterator
            .asSequence()
            .map {
                postResponseMapper.apply(
                    it,
                    checkIsSelf(requesterId, it),
                    checkIsVoted(voteFilter, requesterId, it)
                )
            }
            .toList()
            .let {
                CollectionResponse.builder<PostResponse>()
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(queryResultIterator.cursor.toWebSafeString())
                    .build()
            }
    }

    private fun checkIsSelf(requesterId: Long, post: Post): Boolean {
        return requesterId == post.author.id
    }

    private fun checkIsVoted(voteFilter: NanoCuckooFilter, requesterId: Long, post: Post): Boolean {
        return voteFilter.contains(Vote.createId(requesterId, post.id, "p"))
    }
}