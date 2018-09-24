package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.google.cloud.datastore.Cursor
import com.google.cloud.datastore.QueryResults
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.vo.CommentCollectionResponse
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Post
import com.yoloo.server.like.entity.Like
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class ListCommentsUseCase(
    private val commentResponseMapper: CommentResponseMapper,
    private val memcacheService: MemcacheService
) {

    fun execute(requesterId: Long, postId: Long, cursor: String?): CommentCollectionResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val queryResultIterator = getQueryResultIterator(postId, cursor)

        if (!queryResultIterator.hasNext()) {
            return CommentCollectionResponse.builder().data(emptyList()).build()
        }

        val voteFilter = getVoteFilter()

        val approvedComment = getApprovedCommentResponse(post, requesterId, voteFilter)

        return buildCommentCollectionResponse(
            queryResultIterator,
            requesterId,
            voteFilter,
            approvedComment,
            cursor
        )
    }

    private fun getVoteFilter(): NanoCuckooFilter {
        return memcacheService.get(Like.KEY_FILTER_VOTE) as NanoCuckooFilter
    }

    private fun buildCommentCollectionResponse(
        queryResults: QueryResults<Comment>,
        requesterId: Long,
        voteFilter: NanoCuckooFilter,
        approvedComment: CommentResponse?,
        cursor: String?
    ): CommentCollectionResponse {
        return queryResults
            .asSequence()
            .filter { !it.approved }
            .map {
                commentResponseMapper.apply(
                    it,
                    isSelf(requesterId, it),
                    isVoted(voteFilter, requesterId, it)
                )
            }
            .toList()
            .let {
                CommentCollectionResponse.builder()
                    .approvedComment(approvedComment)
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(queryResults.cursorAfter.toUrlSafe())
                    .build()
            }
    }

    private fun getQueryResultIterator(
        postId: Long,
        cursor: String?
    ): QueryResults<Comment> {
        var query = ofy()
            .load()
            .type(Comment::class.java)
            .filter(Comment.INDEX_POST_ID, postId)
            .orderKey(true)

        cursor?.let { query = query.startAt(Cursor.fromUrlSafe(it)) }
        query = query.limit(50)

        return query.iterator()
    }

    private fun getApprovedCommentResponse(
        post: Post,
        requesterId: Long,
        voteFilter: NanoCuckooFilter
    ): CommentResponse? {
        return post.approvedCommentId?.let {
            val comment = ofy().load().type(Comment::class.java).id(it.value).now()
            return@let commentResponseMapper.apply(
                comment,
                isSelf(requesterId, comment),
                isVoted(voteFilter, requesterId, comment)
            )
        }
    }

    private fun isSelf(requesterId: Long, comment: Comment): Boolean {
        return requesterId == comment.author.id
    }

    private fun isVoted(
        voteFilter: NanoCuckooFilter,
        requesterId: Long,
        comment: Comment
    ): Boolean {
        return voteFilter.contains(Like.createId(requesterId, comment.id))
    }
}
