package com.yoloo.server.feed.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.google.cloud.datastore.Cursor
import com.google.cloud.datastore.QueryResults
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
                    Like.KEY_FILTER_VOTE,
                    Bookmark.KEY_FILTER_BOOKMARK
                )
            ) as Map<String, *>

        val voteFilter = cacheMap[Like.KEY_FILTER_VOTE] as NanoCuckooFilter
        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter

        return buildCollectionResponse(
            queryResultIterator,
            requesterId,
            voteFilter,
            bookmarkFilter,
            cursor
        )
    }

    private fun buildQueryResultIterator(cursor: String?): QueryResults<Post> {
        var query = ofy()
            .load()
            .type(Post::class.java)
            .filter("${Post.INDEX_BOUNTY} !=", null)
            .orderKey(true)

        cursor?.let { query = query.startAt(Cursor.fromUrlSafe(it)) }
        query = query.limit(50)

        return query.iterator()
    }

    private fun buildCollectionResponse(
        queryResults: QueryResults<Post>,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        bookmarkFilter: NanoCuckooFilter,
        cursor: String?
    ): CollectionResponse<PostResponse> {
        return queryResults
            .asSequence()
            .map { mapToPostResponse(it, requesterId, voteFilter, bookmarkFilter) }
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
        it: Post,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        bookmarkFilter: NanoCuckooFilter
    ): PostResponse {
        return postResponseMapper.apply(
            it,
            isSelf(requesterId, it),
            Like.isVoted(voteFilter, requesterId, it.id),
            Bookmark.isBookmarked(bookmarkFilter, requesterId, it.id)
        )
    }

    private fun isSelf(requesterId: Long, post: Post): Boolean {
        return requesterId == post.author.id
    }
}
