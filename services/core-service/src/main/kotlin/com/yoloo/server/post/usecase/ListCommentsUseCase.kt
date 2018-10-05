package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.google.cloud.datastore.QueryResults
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.Exceptions.checkException
import com.yoloo.server.common.applyCursor
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.filter.EntityIdFilter
import com.yoloo.server.filter.FilterService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.user.exception.UserErrors
import com.yoloo.spring.autoconfiguration.appengine.services.counter.CounterService
import org.springframework.stereotype.Service
import org.zalando.problem.Status

@Service
class ListCommentsUseCase(
    private val hashids: Hashids,
    private val filterService: FilterService,
    private val commentResponseMapper: CommentResponseMapper,
    private val counterService: CounterService
) : AbstractUseCase<ListCommentsUseCase.Input, CollectionResponse<CommentResponse>>() {

    override fun onExecute(input: Input): CollectionResponse<CommentResponse> {
        val postId = hashids.decode(input.postId)[0]

        val entityCache = filterService.getAll()

        checkException(entityCache.contains(input.requesterId), Status.NOT_FOUND, UserErrors.NOT_FOUND)
        checkException(entityCache.contains(postId), Status.NOT_FOUND, PostErrors.NOT_FOUND)

        val queryResults = queryResults(postId, input.cursor)

        if (!queryResults.hasNext()) {
            return CollectionResponse.builder<CommentResponse>().build()
        }

        return queryResults
            .asSequence()
            .map {
                it.apply {
                    liked = isLiked(entityCache, input.requesterId, id)
                    approved = isApproved(entityCache, id)
                    likes = 0
                }
            }
            .map(commentResponseMapper::apply)
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

    private fun isLiked(entityIdFilter: EntityIdFilter, requesterId: Long, commentId: Long): Boolean {
        return entityIdFilter.contains(Like.createId(requesterId, commentId))
    }

    private fun isApproved(entityIdFilter: EntityIdFilter, commentId: Long): Boolean {
        return entityIdFilter.contains("APPROVED:$commentId")
    }

    data class Input(val requesterId: Long, val postId: String, val cursor: String?)
}
