package com.yoloo.server.feed.service

import com.yoloo.server.common.vo.response.CollectionResponse
import com.yoloo.server.common.vo.response.ResponseData
import com.yoloo.server.common.vo.response.ResponseMeta
import com.yoloo.server.feed.mapper.PostResponseMapper
import com.yoloo.server.feed.repository.PostRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class FeedService(private val postRepository: PostRepository, private val postResponseMapper: PostResponseMapper) {

    fun listAnonymousFeed(cursor: String?): CollectionResponse {
        return postRepository
            .findAll()
            .stream()
            .map { postResponseMapper.apply(it, false, false, false) }
            .map { ResponseData.create("DEFAULT_POST", it) }
            .collect(Collectors.toList())
            .let { CollectionResponse.create(ResponseMeta.newBuilder().build(), it) }
    }
}