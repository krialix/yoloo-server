package com.yoloo.server.post.usecase

import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory.LongIdGenerator
import com.yoloo.server.common.vo.Author
import com.yoloo.server.group.entity.Group
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import com.yoloo.server.user.entity.User
import org.springframework.stereotype.Service

@Service
class CreatePostUseCase(
    private val idGenerator: LongIdGenerator,
    private val postResponseMapper: PostResponseMapper
) {

    fun execute(requesterId: Long, request: CreatePostRequest): PostResponse {
        val userKey = User.createKey(requesterId)
        val groupKey = Key.create(Group::class.java, request.groupId!!)
        val map = ofy().load().keys(userKey, groupKey) as Map<*, *>

        val user = map[userKey] as User
        val group = map[groupKey] as Group

        val post = createPost(request, user, group)

        ofy().save().entities(post, group)

        addToSearchQueue(post)
        addToNotificationQueue(post)

        return postResponseMapper.apply(post, true, false, false)
    }

    private fun addToNotificationQueue(post: Post) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.email)
            .addData("content", post.content.email)
            .addData("authorDisplayName", post.author.displayName)
            .addData("topic", topicName)
            .build()

        notificationQueueService.addQueueAsync(event)*/
    }

    private fun addToSearchQueue(post: Post) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.email)
            .addData("content", post.content.email)
            .addData("tags", post.tags)
            .build()

        searchQueueService.addQueueAsync(event)*/
    }

    private fun createPost(request: CreatePostRequest, user: User, group: Group): Post {
        return Post(
            id = idGenerator.generateId(),
            author = Author(
                id = user.id,
                displayName = user.profile.displayName.value,
                profileImageUrl = user.profile.profileImageUrl
            ),
            content = PostContent(request.content!!),
            title = PostTitle(request.title!!),
            group = PostGroup(group.id, group.displayName.value),
            tags = request.tags!!.toSet(),
            bounty = if (request.coin == 0) null else PostBounty(request.coin),
            medias = emptyList() // TODO: Implement media upload
        )
    }
}
