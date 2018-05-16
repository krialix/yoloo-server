package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class GetPostUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) {
    fun execute(principal: Principal?, postId: Long): PostResponse {
        val postKey = Key.create(Post::class.java, postId)
        val post = ofy().load().key(postKey).now()

        ServiceExceptions.checkNotFound(post != null, "post.not-found")
        ServiceExceptions.checkNotFound(!post.isDeleted(), "post.not-found")

        return postResponseMapper.apply(post, mutableMapOf("voteDir" to 1))
    }
}