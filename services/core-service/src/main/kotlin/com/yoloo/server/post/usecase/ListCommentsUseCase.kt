package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.google.cloud.datastore.QueryResults
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.Exceptions
import com.yoloo.server.common.applyCursor
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.entity.service.EntityCache
import com.yoloo.server.entity.service.EntityCacheService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import com.yoloo.server.user.exception.UserErrors
import org.springframework.stereotype.Service

@Service
class ListCommentsUseCase(
    private val hashids: Hashids,
    private val entityCacheService: EntityCacheService,
    private val commentResponseMapper: CommentResponseMapper
) : AbstractUseCase<ListCommentsUseCase.Input, CollectionResponse<CommentResponse>>() {

    override fun onExecute(input: Input): CollectionResponse<CommentResponse> {
        val postId = hashids.decode(input.postId)[0]

        val entityCache = entityCacheService.get()

        Exceptions.checkException(entityCache.contains(input.requesterId),  UserErrors.NOT_FOUND)
        checkNotFound(entityCache.contains(postId), PostErrors.NOT_FOUND)

        val post = ofy().load().key(Post.createKey(postId)).now()

        val queryResults = queryResults(postId, input.cursor)

        if (!queryResults.hasNext()) {
            return CollectionResponse.builder<CommentResponse>().build()
        }

        return queryResults
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
            .let {
                CollectionResponse.builder<CommentResponse>()
                    .data(it)
                    .prevPageToken(input.cursor)
                    .nextPageToken(queryResults.cursorAfter.toUrlSafe())
                    .build()
            }
    }

    private fun queryResults(postId: Long, cursor: String?): QueryResults<Comment> {
        return ofy()
            .load()
            .type(Comment::class.java)
            .filter(Comment.INDEX_POST_ID, postId)
            .order("-${Comment.INDEX_RANK}")
            .applyCursor(cursor)
            .limit(50)
            .iterator()
    }

    private fun isLiked(entityCache: EntityCache, requesterId: Long, commentId: Long): Boolean {
        return entityCache.contains(Like.createId(requesterId, commentId))
    }

    data class Input(val requesterId: Long, val postId: String, val cursor: String?) : UseCase.Input
}
