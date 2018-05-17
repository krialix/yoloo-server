package com.yoloo.server.post.usecase

import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class InsertPostUseCase(
    private val postResponseMapper: PostResponseMapper,
    @Qualifier("cached") private val idGenerator: LongIdGenerator,
    private val userInfoFetcher: Fetcher<Long, UserInfoResponse>,
    private val groupInfoFetcher: Fetcher<Long, GroupInfoResponse>
) {
    fun execute(jwtClaims: JwtClaims, request: InsertPostRequest): PostResponse {
        val userId = jwtClaims.sub

        val userInfoResponse = userInfoFetcher.fetch(userId)
        val groupInfoResponse = groupInfoFetcher.fetch(request.groupId)

        val post = Post(
            id = idGenerator.generateId(),
            type = findPostType(request),
            author = Author(
                id = userId,
                displayName = jwtClaims.displayName,
                avatar = AvatarImage(Url(jwtClaims.picture)),
                verified = userInfoResponse.verified
            ),
            content = PostContent(request.content!!),
            title = PostTitle(request.title!!),
            group = PostGroup(groupInfoResponse.id, groupInfoResponse.displayName),
            tags = request.tags!!.map(::PostTag).toSet(),
            coin = if (request.coin == 0) null else PostCoin(request.coin),
            buddyRequest = when (request.buddyInfo) {
                null -> null
                else -> createBuddyRequest(request.buddyInfo)
            }
        )

        // todo inc post count of the user

        return postResponseMapper.apply(post)
    }

    private fun findPostType(request: InsertPostRequest): PostType {
        if (request.buddyInfo != null) {
            return PostType.BUDDY
        }
        if (request.attachments != null) {
            return PostType.ATTACHMENT
        }

        return PostType.TEXT
    }

    private fun createBuddyRequest(buddyInfo: BuddyInfo): BuddyRequest {
        return BuddyRequest(
            peopleRange = Range(buddyInfo.fromPeople!!, buddyInfo.toPeople!!),
            location = Location(buddyInfo.location!!),
            dateRange = Range(buddyInfo.fromDate!!, buddyInfo.toDate!!)
        )
    }
}