package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.PostTag
import com.yoloo.server.post.vo.UpdatePostRequest
import com.yoloo.server.rest.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class UpdatePostUseCase(
    private val postResponseMapper: PostResponseMapper,
    private val memcacheService: MemcacheService,
    private val objectMapper: ObjectMapper,
    private val pubSubTemplate: PubSubTemplate
) {

    fun execute(requesterId: Long, postId: Long, request: UpdatePostRequest): PostResponse {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val self = requesterId == post.author.id

        ServiceExceptions.checkForbidden(self, "post.forbidden_update")

        val cacheMap = getCache()

        request.content?.let { post.content.value = it }
        request.tags?.map { PostTag(it) }?.toSet()?.let { post.tags = it }

        val voteFilter = cacheMap[Vote.KEY_FILTER_VOTE] as NanoCuckooFilter
        val voted = Vote.isVoted(voteFilter, requesterId, postId, "p")

        val bookmarkFilter = cacheMap[Bookmark.KEY_FILTER_BOOKMARK] as NanoCuckooFilter
        val bookmarked = Bookmark.isBookmarked(bookmarkFilter, requesterId, postId)

        ofy().save().entity(post)

        publishPostUpdatedEvent(post)

        return postResponseMapper.apply(post, self, voted, bookmarked)
    }

    private fun getCache(): Map<String, *> {
        return memcacheService.getAll(
            listOf(
                Vote.KEY_FILTER_VOTE,
                Bookmark.KEY_FILTER_BOOKMARK
            )
        ) as Map<String, *>
    }

    private fun publishPostUpdatedEvent(post: Post) {
        val json = objectMapper.writeValueAsString(post)
        pubSubTemplate.publish("post.update", json, null)
    }
}