package com.yoloo.server.post.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.vo.EventType
import com.yoloo.server.common.queue.vo.YolooEvent
import com.yoloo.server.common.queue.service.NotificationQueueService
import com.yoloo.server.common.queue.service.SearchQueueService
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.group.entity.Group
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import com.yoloo.server.user.entity.User

class CreatePostUseCase(
    private val idGenerator: LongIdGenerator,
    private val postResponseMapper: PostResponseMapper,
    private val searchQueueService: SearchQueueService,
    private val notificationQueueService: NotificationQueueService
) {

    fun execute(requesterId: Long, request: CreatePostRequest): PostResponse {
        val userKey = User.createKey(requesterId)
        val groupKey = Key.create(Group::class.java, request.groupId!!)
        val map = ofy().load().keys(userKey, groupKey) as Map<*, *>

        val user = map[userKey] as User
        val group = map[groupKey] as Group

        group.countData.postCount = group.countData.postCount.inc()
        user.profile.countData.postCount = user.profile.countData.postCount.inc()

        val post = createPost(request, user, group)

        val saveResult = ofy().save().entities(post, group)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
        addToNotificationQueue(post, group.topicName)

        return postResponseMapper.apply(post, true, false, false)
    }

    private fun addToNotificationQueue(post: Post, topicName: String) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.value)
            .addData("content", post.content.value)
            .addData("authorDisplayName", post.author.displayName)
            .addData("topic", topicName)
            .build()

        notificationQueueService.addQueueAsync(event)
    }

    private fun addToSearchQueue(post: Post) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.value)
            .addData("content", post.content.value)
            .addData("tags", post.tags.map { it.value })
            .addData("buddyRequest", post.buddyRequest)
            .build()

        searchQueueService.addQueueAsync(event)
    }

    private fun createPost(
        request: CreatePostRequest,
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
            bounty = if (request.coin == 0) null else PostBounty(request.coin),
            buddyRequest = when (request.buddyInfo) {
                null -> null
                else -> createBuddyRequest(request.buddyInfo)
            }
        )
    }

    private fun findPostType(request: CreatePostRequest): PostType {
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