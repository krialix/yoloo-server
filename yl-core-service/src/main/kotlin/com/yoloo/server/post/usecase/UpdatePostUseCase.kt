package com.yoloo.server.post.usecase

import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.queue.vo.EventType
import com.yoloo.server.common.queue.vo.YolooEvent
import com.yoloo.server.common.queue.service.SearchQueueService
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.PostTag
import com.yoloo.server.post.vo.UpdatePostRequest

class UpdatePostUseCase(private val searchQueueService: SearchQueueService) {

    fun execute(requesterId: Long, postId: Long, request: UpdatePostRequest) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val self = requesterId == post.author.id

        ServiceExceptions.checkForbidden(self, "post.forbidden_update")

        request.content?.let { post.content.value = it }
        request.tags?.map { PostTag(it) }?.toSet()?.let { post.tags = it }

        val saveResult = ofy().save().entity(post)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
    }

    private fun addToSearchQueue(post: Post) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.UPDATE_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.value)
            .addData("content", post.content.value)
            .addData("tags", post.tags.map { it.value })
            .addData("buddyRequest", post.buddyRequest)
            .build()

        searchQueueService.addQueueAsync(event)
    }
}