package com.yoloo.server.post.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.common.event.PubSubEvent
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.group.entity.Group
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import com.yoloo.server.user.entity.User
import org.springframework.context.ApplicationEventPublisher

class InsertPostUseCase(
    private val idGenerator: LongIdGenerator,
    private val postResponseMapper: PostResponseMapper,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun execute(requesterId: Long, request: InsertPostRequest): PostResponse {
        val userKey = User.createKey(requesterId)
        val groupKey = Key.create(Group::class.java, request.groupId!!)
        val map = ofy().load().keys(userKey, groupKey) as Map<*, *>

        val user = map[userKey] as User
        val group = map[groupKey] as Group

        group.countData.postCount = group.countData.postCount.inc()
        user.profile.countData.postCount = user.profile.countData.postCount.inc()

        val post = createPost(request, user, group)

        val saveResult = ofy().save().entities(post, group)
        TestUtil.saveResultsNowIfTest(saveResult)

        eventPublisher.publishEvent(PubSubEvent("post.create", post, this))

        return postResponseMapper.apply(post, true, false, false)
    }

    private fun createPost(
        request: InsertPostRequest,
        user: User,
        group: Group
    ): Post {
        return Post(
            id = idGenerator.generateId(),
            type = findPostType(request),
            author = Author(
                id = user.id,
                displayName = user.profile.displayName.value,
                avatar = user.profile.image
            ),
            content = PostContent(request.content!!),
            title = PostTitle(request.title!!),
            group = PostGroup(group.id, group.displayName.value),
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
}