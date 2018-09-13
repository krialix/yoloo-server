package com.yoloo.server.post.service

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.bookmark.entity.Bookmark
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.common.vo.Author
import com.yoloo.server.group.entity.Group
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import com.yoloo.server.user.entity.User
import com.yoloo.server.vote.entity.Vote
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService,
    private val idGenerator: LongIdGenerator,
    private val pubSubTemplate: PubSubTemplate
) : PostService {

    @Cacheable("posts")
    override fun getPost(userId: Long, postId: Long): PostResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val cacheMap =
            memcacheService.getAll(listOf(Vote.KEY_FILTER_VOTE, Bookmark.KEY_FILTER_BOOKMARK)) as Map<String, *>

        val self = userId == post.author.id

        val voteFilter = cacheMap[Vote.KEY_FILTER_VOTE] as NanoCuckooFilter
        val voted = Vote.isVoted(voteFilter, userId, postId, "p")

        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter
        val bookmarked = Bookmark.isBookmarked(bookmarkFilter, userId, postId)

        return postResponseMapper.apply(post, self, voted, bookmarked)
    }

    override fun createPost(userId: Long, request: CreatePostRequest): PostResponse {
        val userKey = User.createKey(userId)
        val groupKey = Group.createKey(request.groupId!!)
        val map = ofy().load().keys(userKey, groupKey) as Map<*, *>

        val user = map[userKey] as User
        val group = map[groupKey] as Group

        // TODO: find more scalable way
        group.countData.postCount = group.countData.postCount.inc()
        user.profile.countData.postCount = user.profile.countData.postCount.inc()

        val post = createPost(request, user, group)

        val saveResult = ofy().save().entities(post, group)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
        addToNotificationQueue(post, group.topicName)

        return postResponseMapper.apply(post, true, false, false)
    }

    override fun updatePost(userId: Long, postId: Long, request: UpdatePostRequest) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val self = userId == post.author.id

        ServiceExceptions.checkForbidden(self, "post.forbidden_update")

        request.content?.let { post.content.value = it }
        request.tags?.toSet()?.let { post.tags = it }

        val saveResult = ofy().save().entity(post)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
    }

    override fun deletePost(userId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == userId, "post.forbidden")

        post.markAsDeleted()

        val saveResult = ofy().save().entity(post)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
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
            buddyRequest = when (request.buddyInfo) {
                null -> null
                else -> BuddyRequest(
                    peopleRange = Range(request.buddyInfo.fromPeople!!, request.buddyInfo.toPeople!!),
                    location = Location(request.buddyInfo.location!!),
                    dateRange = Range(request.buddyInfo.fromDate!!, request.buddyInfo.toDate!!)
                )
            },
            medias = emptyList() // TODO: Implement media upload
        )
    }

    private fun addToNotificationQueue(post: Post, topicName: String) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.value)
            .addData("content", post.content.value)
            .addData("authorDisplayName", post.author.displayName)
            .addData("topic", topicName)
            .build()

        notificationQueueService.addQueueAsync(event)*/
    }

    private fun addToSearchQueue(post: Post) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.NEW_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.value)
            .addData("content", post.content.value)
            .addData("tags", post.tags)
            .addData("buddyRequest", post.buddyRequest)
            .build()

        searchQueueService.addQueueAsync(event)*/
    }
}
