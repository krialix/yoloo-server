package com.yoloo.server.post.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.datastore.QueryResultIterator
import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter

class ListBookmarkedPostsUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {

    fun execute(requesterId: Long, cursor: String?): CollectionResponse<PostResponse> {
        val queryResultIterator = buildQueryResultIterator(requesterId, cursor)

        if (!queryResultIterator.hasNext()) {
            return CollectionResponse.builder<PostResponse>().data(emptyList()).build()
        }

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE) as NanoCuckooFilter

        return buildCollectionResponse(queryResultIterator, requesterId, voteFilter, cursor)
    }

    private fun buildQueryResultIterator(
        requesterId: Long,
        cursor: String?
    ): QueryResultIterator<Key<Bookmark>> {
        var query = ofy()
            .load()
            .type(Bookmark::class.java)
            .filter(Bookmark.INDEX_USER_ID, requesterId)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        return query.keys().iterator()
    }

    private fun buildCollectionResponse(
        queryResultIterator: QueryResultIterator<Key<Bookmark>>,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        cursor: String?
    ): CollectionResponse<PostResponse> {
        return queryResultIterator
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
                    .nextPageToken(queryResultIterator.cursor.toWebSafeString())
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
            Vote.isVoted(voteFilter, requesterId, post.id, "p"),
            true
        )
    }

    private fun isSelf(requesterId: Long, post: Post): Boolean {
        return requesterId == post.author.id
    }
}