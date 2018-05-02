package com.yoloo.server.post.domain.usecase

import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.request.PostRequest
import com.yoloo.server.post.domain.response.PostResponse
import com.yoloo.server.post.domain.response.UserInfoResponse
import com.yoloo.server.post.domain.vo.*
import com.yoloo.server.post.domain.vo.postdata.TextPostData
import com.yoloo.server.post.infrastructure.mapper.PostResponseMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class InsertPostUseCase(
    private val postResponseMapper: PostResponseMapper,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<UserInfoResponse>
) : UseCase<InsertPostUseCase.Request, PostResponse> {

    override fun execute(request: Request): PostResponse {
        val userId = request.principal!!.name.toLong()

        val userInfoResponse = userInfoFetcher.fetch(userId)

        // todo fetch groups

        val payload = request.payload

        val post = Post(
            id = idGenerator.generateId(),
            author = Author(id = userId, displayName = "user", avatarUrl = ""),
            data = TextPostData(
                title = PostTitle(payload.title),
                group = PostGroup(id = "id", displayName = "group"),
                tags = payload.tags.map(::PostTag).toSet()
            ),
            content = PostContent(payload.content)
        )

        return postResponseMapper.apply(post)
    }

    class Request(val principal: Principal?, val payload: PostRequest)
}