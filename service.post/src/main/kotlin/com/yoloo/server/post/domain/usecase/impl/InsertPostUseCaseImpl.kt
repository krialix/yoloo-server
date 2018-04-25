package com.yoloo.server.post.domain.usecase.impl

import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.usecase.InsertPostUseCase
import com.yoloo.server.post.domain.usecase.contract.InsertPostContract
import com.yoloo.server.post.domain.vo.*
import com.yoloo.server.post.domain.vo.postdata.TextPostData
import com.yoloo.server.post.infrastructure.mapper.PostResponseMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class InsertPostUseCaseImpl(
    private val postResponseMapper: PostResponseMapper,
    @Qualifier("cached") private val idGenerator: LongIdGenerator
) : InsertPostUseCase {

    override fun execute(request: InsertPostContract.Request): InsertPostContract.Response {
        val userId = request.principal!!.name

        // todo fetch groups

        val postRequest = request.postRequest

        val post = Post(
            id = idGenerator.generateId(),
            author = Author(id = "id", displayName = "user", avatarUrl = ""),
            data = TextPostData(
                title = PostTitle(postRequest.title),
                group = PostGroup(id = "id", displayName = "group"),
                tags = postRequest.tags.map(::PostTag).toSet()
            ),
            content = PostContent(postRequest.content)
        )

        val response = postResponseMapper.apply(post)

        return InsertPostContract.Response(response)
    }
}