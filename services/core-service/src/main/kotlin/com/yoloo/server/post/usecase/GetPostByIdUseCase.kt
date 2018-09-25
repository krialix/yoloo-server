package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class GetPostByIdUseCase(
    private val hashIds: Hashids,
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) : AbstractUseCase<GetPostByIdUseCase.Input, PostResponse>() {

    override fun onExecute(input: Input): PostResponse {
        val postId = hashIds.decode(input.postId)[0]
        val userId = hashIds.decode(input.requesterId)[0]
    }

    fun execute(requesterId: Long, postId: Long): PostResponse {
        val post = ofy().load().key(Post.createKey(postId)).now()

        ServiceExceptions.checkNotFound(post != null, PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkNotFound(!post.isSoftDeleted, PostErrors.ERROR_POST_NOT_FOUND)

        val cacheMap =
            memcacheService.getAll(listOf(Like.KEY_FILTER_VOTE, Bookmark.KEY_FILTER_BOOKMARK)) as Map<String, *>

        val self = requesterId == post.author.id

        val voteFilter = cacheMap[Like.KEY_FILTER_VOTE] as NanoCuckooFilter
        val voted = Like.isVoted(voteFilter, requesterId, postId)

        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter
        val bookmarked = Bookmark.isBookmarked(bookmarkFilter, requesterId, postId)

        return postResponseMapper.apply(post, PostResponseMapper.Params(self, voted, bookmarked))
    }

    data class Input(val requesterId: String, val postId: String) : UseCase.Input {
        override fun getRequestId(): String {
            return "$requesterId:$postId"
        }
    }
}
