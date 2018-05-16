package com.yoloo.server.post.usecase

import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.PostRequest
import com.yoloo.server.post.vo.GroupInfoResponse
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.UserInfoResponse
import com.yoloo.server.post.vo.*
import com.yoloo.server.post.vo.postdata.TextPostData
import com.yoloo.server.post.mapper.PostResponseMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class InsertPostUseCase(
    private val postResponseMapper: PostResponseMapper,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<Long, UserInfoResponse>,
    private val groupInfoFetcher: Fetcher<Long, GroupInfoResponse>
) : UseCase<InsertPostUseCase.Request, PostResponse> {

    override fun execute(request: Request): PostResponse {
        val userId = request.principal!!.name.toLong()

        val userInfoResponse = userInfoFetcher.fetch(userId)
        val groupInfoResponse = groupInfoFetcher.fetch(request.payload.groupId)

        val payload = request.payload

        val post = Post(
            id = idGenerator.generateId(),
            author = Author(
                id = userId,
                displayName = userInfoResponse.displayName,
                avatar = AvatarImage(Url(userInfoResponse.image)),
                verified = userInfoResponse.verified
            ),
            data = TextPostData(
                title = PostTitle(payload.title!!),
                group = PostGroup(groupInfoResponse.id, groupInfoResponse.displayName),
                tags = payload.tags!!.map(::PostTag).toSet()
            ),
            content = PostContent(payload.content!!)
        )

        // todo inc post count of the user

        return postResponseMapper.apply(post)
    }

    class Request(val principal: Principal?, val payload: PostRequest)
}