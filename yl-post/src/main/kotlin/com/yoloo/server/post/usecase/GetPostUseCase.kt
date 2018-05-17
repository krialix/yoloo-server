package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.JwtClaims
import com.yoloo.server.post.vo.PostResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class GetPostUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {
    fun execute(jwtClaims: JwtClaims, postId: Long): PostResponse {
        val requesterId = jwtClaims.sub
        val postKey = Key.create(Post::class.java, postId)
        val post = ofy().load().key(postKey).now()

        ServiceExceptions.checkNotFound(post != null, "post.not-found")
        ServiceExceptions.checkNotFound(!post.isDeleted(), "post.not-found")

        val voted = checkIsVoted(requesterId, postId)
        return postResponseMapper.apply(post, voted)
    }

    private fun checkIsVoted(requesterId: Long, postId: Long): Boolean {
        val voteFilter = memcacheService.get(Filters.KEY_FILTER_POST_VOTE) as NanoCuckooFilter
        return voteFilter.contains("$requesterId:$postId")
    }
}