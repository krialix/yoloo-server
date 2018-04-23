package com.yoloo.server.post.domain.usecase.impl

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.PostResponse
import com.yoloo.server.post.domain.usecase.ListTopicPostsUseCase
import com.yoloo.server.post.domain.usecase.contract.ListTopicPostsContract
import com.yoloo.server.post.infrastructure.mapper.PostResponseMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListTopicPostsUseCaseImpl @Autowired constructor(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService
) : ListTopicPostsUseCase {

    override fun execute(request: ListTopicPostsContract.Request): ListTopicPostsContract.Response {
        var query = ofy()
            .load()
            .type(Post::class.java)
            .filter("data.group.id", request.topicId)
            .orderKey(true)

        request.cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }
        query = query.limit(50)

        val iterator = query.iterator()

        val data = iterator.asSequence()
            .map { postResponseMapper.apply(it) }
            .toList()

        val response = CollectionResponse.builder<PostResponse>()
            .data(data)
            .prevPageToken(request.cursor)
            .nextPageToken(iterator.cursor.toWebSafeString())
            .build()

        return ListTopicPostsContract.Response(response)
    }
}