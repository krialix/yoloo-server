package com.yoloo.server.post.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.PostResponse
import com.yoloo.server.post.infrastructure.mapper.PostResponseMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class GetPostUseCase @Autowired constructor(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) : UseCase<GetPostUseCase.Request, PostResponse> {

    override fun execute(request: Request): PostResponse {
        val postKey = Key.create(Post::class.java, request.postId)
        val post = ofy().load().key(postKey).now()

        if (post == null || post.isDeleted()) {
            throw NotFoundException("post.not-found")
        }

        return postResponseMapper.apply(post, mutableMapOf("voteDir" to 1))
    }

    class Request(val principal: Principal?, val postId: String)
}