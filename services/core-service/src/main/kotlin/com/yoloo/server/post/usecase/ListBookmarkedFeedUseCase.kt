package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.google.cloud.datastore.Cursor
import com.google.cloud.datastore.QueryResults
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.like.entity.Like
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class ListBookmarkedFeedUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {

    fun execute(requesterId: Long, cursor: String?): CollectionResponse<PostResponse> {
        val queryResults = buildQueryResultIterator(requesterId, cursor)

        if (!queryResults.hasNext()) {
            return CollectionResponse.builder<PostResponse>().data(emptyList()).build()
        }

        val voteFilter = memcacheService.get(Like.KEY_FILTER_VOTE) as NanoCuckooFilter

        return buildCollectionResponse(queryResults, requesterId, voteFilter, cursor)
    }

    private fun buildQueryResultIterator(
        requesterId: Long,
        cursor: String?
    ): QueryResults<Key<Bookmark>> {
        var query = ofy()
            .load()
            .type(Bookmark::class.java)
            .filter(Bookmark.INDEX_USER_ID, requesterId)

        cursor?.let { query = query.startAt(Cursor.fromUrlSafe(cursor)) }
        query = query.limit(50)

        return query.keys().iterator()
    }

    private fun buildCollectionResponse(
        queryResults: QueryResults<Key<Bookmark>>,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        cursor: String?
    ): CollectionResponse<PostResponse> {
        return queryResults
            .asSequence()
            .map { Bookmark.getPostKey(it) }
            .toList()
            .let { ofy().load().keys(it).values }
            .map { mapToPostResponse(it, requesterId, voteFilter) }
            .toList()
            .let {
                CollectionResponse.builder<PostResponse>()
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(queryResults.cursorAfter.toUrlSafe())
                    .build()
            }
    }

    private fun mapToPostResponse(
        post: Post,
        requesterId: Long,
        voteFilter: NanoCuckooFilter
    ): PostResponse {
        return postResponseMapper.apply(
            post,
            isSelf(requesterId, post),
            Like.isVoted(voteFilter, requesterId, post.id),
            true
        )
    }

    private fun isSelf(requesterId: Long, post: Post): Boolean {
        return requesterId == post.author.id
    }
}
