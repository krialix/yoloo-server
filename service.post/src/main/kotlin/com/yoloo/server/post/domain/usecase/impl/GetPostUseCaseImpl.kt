package com.yoloo.server.post.domain.usecase.impl

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.usecase.GetPostUseCase
import com.yoloo.server.post.domain.usecase.contract.GetPostContract
import com.yoloo.server.post.infrastructure.mapper.PostResponseMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetPostUseCaseImpl @Autowired constructor(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) : GetPostUseCase {

    override fun execute(request: GetPostContract.Request): GetPostContract.Response {
        val postKey = Key.create(Post::class.java, request.postId)
        val post = ofy().load().key(postKey).now()

        if (post == null || post.isDeleted()) {
            throw NotFoundException("post.not-found")
        }

        val response = postResponseMapper.apply(post, mutableMapOf("voteDir" to 1))

        return GetPostContract.Response(response)
    }
}