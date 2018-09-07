package com.yoloo.server.feed.usecase

import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.util.CircularFifoBuffer
import com.yoloo.server.post.vo.PostResponse
import org.springframework.stereotype.Service

@Service
class ListAnonymousMainFeedUseCase(
    private val anonymousFeedHolder: CircularFifoBuffer,
    private val postResponseMapper: PostResponseMapper
) {

    fun execute(): CollectionResponse<PostResponse> {
        return ofy()
            .load()
            .type(Post::class.java)
            .ids(*anonymousFeedHolder.elements.toTypedArray())
            .values
            .asSequence()
            .map { postResponseMapper.apply(it, false, false, false) }
            .toList()
            .let {
                CollectionResponse.builder<PostResponse>()
                    .data(it)
                    .build()
            }

    }
}
