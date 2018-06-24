package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.queue.api.EventType
import com.yoloo.server.common.queue.api.YolooEvent
import com.yoloo.server.common.queue.config.QueueEndpoint
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post

class DeletePostUseCase(
    private val searchQueue: Queue,
    private val objectMapper: ObjectMapper
) {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden")

        post.markAsDeleted()

        val saveResult = ofy().save().entity(post)
        TestUtil.saveResultsNowIfTest(saveResult)

        addToSearchQueue(post)
    }

    private fun addToSearchQueue(post: Post) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.DELETE_POST))
            .addData("id", post.id.toString())
            .build()

        val json = objectMapper.writeValueAsString(event)
        searchQueue.addAsync(
            TaskOptions.Builder
                .withUrl(QueueEndpoint.QUEUE_SEARCH_ENDPOINT)
                .param("data", json)
        )
    }
}