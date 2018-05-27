package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.rest.error.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class GetPostUseCase(private val postResponseMapper: PostResponseMapper, private val memcacheService: MemcacheService) {

    fun execute(requesterId: Long, postId: Long): PostResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val cacheMap =
            memcacheService.getAll(listOf(Vote.KEY_FILTER_VOTE, Bookmark.KEY_FILTER_BOOKMARK)) as Map<String, *>

        val self = requesterId == post.author.id

        val voteFilter = cacheMap[Vote.KEY_FILTER_VOTE] as NanoCuckooFilter
        val voted = Vote.isVoted(voteFilter, requesterId, postId, "p")

        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter
        val bookmarked = Bookmark.isBookmarked(bookmarkFilter, requesterId, postId)

        return postResponseMapper.apply(post, self, voted, bookmarked)
    }
}