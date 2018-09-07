package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.bookmark.entity.Bookmark
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.vote.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class GetPostByIdUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {

    fun execute(requesterId: Long, postId: Long): PostResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val cacheMap = getCache()

        val self = requesterId == post.author.id

        val voteFilter = cacheMap[Vote.KEY_FILTER_VOTE] as NanoCuckooFilter
        val voted = Vote.isVoted(voteFilter, requesterId, postId, "p")

        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter
        val bookmarked = Bookmark.isBookmarked(bookmarkFilter, requesterId, postId)

        return postResponseMapper.apply(post, self, voted, bookmarked)
    }

    private fun getCache(): Map<String, *> {
        return memcacheService.getAll(listOf(Vote.KEY_FILTER_VOTE, Bookmark.KEY_FILTER_BOOKMARK)) as Map<String, *>
    }
}
