package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.PostTag
import com.yoloo.server.post.vo.UpdatePostRequest
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class UpdatePostUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {
    fun execute(requesterId: Long, postId: Long, request: UpdatePostRequest): PostResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.isDeleted(), "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden_update")

        request.content?.let { post.content.value = it }
        request.tags?.map { PostTag(it) }?.toSet()?.let { post.tags = it }

        val self = requesterId == post.author.id
        val voted = checkIsVoted(requesterId, postId)
        return postResponseMapper.apply(post, self, voted)
    }

    private fun checkIsVoted(requesterId: Long, postId: Long): Boolean {
        val voteFilter = memcacheService.get(Filters.KEY_FILTER_VOTE) as NanoCuckooFilter
        return voteFilter.contains(Vote.createId(requesterId, postId, "p"))
    }
}