package com.yoloo.server.post.usecase

import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.UpdatePostRequest
import org.springframework.stereotype.Service

@Service
class UpdatePostUseCase {

    fun execute(requesterId: Long, postId: Long, request: UpdatePostRequest) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val self = requesterId == post.author.id

        ServiceExceptions.checkForbidden(self, "post.forbidden_update")

        request.content?.let { post.content.value = it }
        request.tags?.toSet()?.let { post.tags = it }

        val saveResult = ofy().save().entity(post)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
    }

    private fun addToSearchQueue(post: Post) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.UPDATE_POST))
            .addData("id", post.id.toString())
            .addData("title", post.title.value)
            .addData("content", post.content.value)
            .addData("tags", post.tags)
            .addData("buddyRequest", post.buddyRequest)
            .build()

        searchQueueService.addQueueAsync(event)*/
    }
}
