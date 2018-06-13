package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.appengine.util.AppengineEnv
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.fetcher.GroupInfoFetcher
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class InsertPostUseCase(
    private val idGenerator: LongIdGenerator,
    private val postResponseMapper: PostResponseMapper,
    private val groupInfoFetcher: GroupInfoFetcher,
    private val pubSubTemplate: PubSubTemplate,
    private val objectMapper: ObjectMapper
) {

    fun execute(requester: Requester, request: InsertPostRequest): PostResponse {
        val groupInfo = groupInfoFetcher.fetch(request.groupId)

        val post = createPost(request, requester, groupInfo)

        // TODO inc group post count
        // TODO If buddy post -> register in buddy search

        val saveFuture = ofy().save().entities(post)

        if (AppengineEnv.isTest()) {
            saveFuture.now()
        }

        publishPostCreatedEvent(post)

        return postResponseMapper.apply(post, true, false, false)
    }

    private fun publishPostCreatedEvent(post: Post) {
        val json = objectMapper.writeValueAsString(post)
        pubSubTemplate.publish("post.create", json, null)
    }

    private fun createPost(
        request: InsertPostRequest,
        requester: Requester,
        groupInfo: GroupInfoResponse
    ): Post {
        return Post(
            id = idGenerator.generateId(),
            type = findPostType(request),
            author = Author(
                id = requester.userId,
                displayName = requester.displayName,
                avatar = AvatarImage(Url(requester.avatarUrl)),
                verified = requester.verified
            ),
            content = PostContent(request.content!!),
            title = PostTitle(request.title!!),
            group = PostGroup(groupInfo.id, groupInfo.displayName),
            tags = request.tags!!.map(::PostTag).toSet(),
            coin = if (request.coin == 0) null else PostCoin(request.coin),
            buddyRequest = when (request.buddyInfo) {
                null -> null
                else -> createBuddyRequest(request.buddyInfo)
            }
        )
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

    data class Requester(
        val userId: Long,
        val displayName: String,
        val avatarUrl: String,
        val verified: Boolean
    )
}