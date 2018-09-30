package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.google.cloud.datastore.Cursor
import com.google.cloud.datastore.QueryResults
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.entity.service.EntityCache
import com.yoloo.server.entity.service.EntityCacheService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.CommentCollectionResponse
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import com.yoloo.server.user.exception.UserErrors
import org.springframework.stereotype.Service

@Service
class ListCommentsUseCase(
    private val hashids: Hashids,
    private val entityCacheService: EntityCacheService,
    private val commentResponseMapper: CommentResponseMapper
) : AbstractUseCase<ListCommentsUseCase.Input, CommentCollectionResponse>() {

    override fun onExecute(input: Input): CommentCollectionResponse {
        val postId = hashids.decode(input.postId)[0]

        val entityCache = entityCacheService.get()

        checkNotFound(entityCache.contains(input.requesterId), UserErrors.NOT_FOUND)
        checkNotFound(entityCache.contains(postId), PostErrors.NOT_FOUND)

        val post = ofy().load().key(Post.createKey(postId)).now()

        val queryResults = queryResults(postId, input.cursor)

        if (!queryResults.hasNext()) {
            return CommentCollectionResponse.builder().data(emptyList()).build()
        }

        val approvedCommentResponse = getApprovedComment(post.approvedCommentId?.value)?.let {
            commentResponseMapper.apply(
                it,
                CommentResponseMapper.Params(
                    it.author.isSelf(input.requesterId),
                    isLiked(entityCache, input.requesterId, it.id)
                )
            )
        }

        val commentsResponse = queryResults
            .asSequence()
            .filter { it.id != post.approvedCommentId?.value }
            .map {
                commentResponseMapper.apply(
                    it,
                    CommentResponseMapper.Params(
                        it.author.isSelf(input.requesterId),
                        isLiked(entityCache, input.requesterId, it.id)
                    )
                )
            }
            .toList()

        return CommentCollectionResponse.builder()
            .approvedComment(approvedCommentResponse)
            .data(commentsResponse)
            .prevPageToken(input.cursor)
            .nextPageToken(queryResults.cursorAfter.toUrlSafe())
            .build()
    }

    private fun queryResults(
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

    private fun getApprovedComment(commentId: Long?): Comment? {
        return commentId?.let { ofy().load().key(Comment.createKey(it)).now() }
    }

    private fun isLiked(entityCache: EntityCache, requesterId: Long, commentId: Long): Boolean {
        return entityCache.contains(Like.createId(requesterId, commentId))
    }

    data class Input(val requesterId: Long, val postId: String, val cursor: String?) : UseCase.Input
}
