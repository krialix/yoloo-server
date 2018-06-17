package com.yoloo.server.post.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.datastore.QueryResultIterator
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.bookmark.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.vote.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter

class ListBountyFeedUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {

    fun execute(requesterId: Long, cursor: String?): CollectionResponse<PostResponse> {
        val queryResultIterator = buildQueryResultIterator(cursor)

        if (!queryResultIterator.hasNext()) {
            return CollectionResponse.builder<PostResponse>().data(emptyList()).build()
        }

        val cacheMap =
            memcacheService.getAll(
                listOf(
                    Vote.KEY_FILTER_VOTE,
                    Bookmark.KEY_FILTER_BOOKMARK
                )
            ) as Map<String, *>

        val voteFilter = cacheMap[Vote.KEY_FILTER_VOTE] as NanoCuckooFilter
        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter

        return buildCollectionResponse(
            queryResultIterator,
            requesterId,
            voteFilter,
            bookmarkFilter,
            cursor
        )
    }

    private fun buildQueryResultIterator(cursor: String?): QueryResultIterator<Post> {
        var query = ofy()
            .load()
            .type(Post::class.java)
            .filter("${Post.INDEX_COIN} !=", null)
            .orderKey(true)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        return query.iterator()
    }

    private fun buildCollectionResponse(
        queryResultIterator: QueryResultIterator<Post>,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        bookmarkFilter: NanoCuckooFilter,
        cursor: String?
    ): CollectionResponse<PostResponse> {
        return queryResultIterator
            .asSequence()
            .map { mapToPostResponse(it, requesterId, voteFilter, bookmarkFilter) }
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
        it: Post,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        bookmarkFilter: NanoCuckooFilter
    ): PostResponse {
        return postResponseMapper.apply(
            it,
            isSelf(requesterId, it),
            Vote.isVoted(voteFilter, requesterId, it.id, "p"),
            Bookmark.isBookmarked(bookmarkFilter, requesterId, it.id)
        )
    }

    private fun isSelf(requesterId: Long, post: Post): Boolean {
        return requesterId == post.author.id
    }
}