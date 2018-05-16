package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.response.PostResponse
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class GetPostUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) : UseCase<GetPostUseCase.Params, PostResponse> {

    override fun execute(params: Params): PostResponse {
        val postKey = Key.create(Post::class.java, params.postId)
        val post = ofy().load().key(postKey).now()

        if (post == null || post.isDeleted()) {
            throw NotFoundException("post.not-found")
        }

        return postResponseMapper.apply(post, mutableMapOf("voteDir" to 1))
    }

    class Params(val principal: Principal?, val postId: Long)
}