package com.yoloo.server.post.usecase

import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.stereotype.Service

@Service
class DeletePostUseCase {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden")

        post.markAsDeleted()

        val saveResult = ofy().save().entity(post)
        TestUtil.saveNow(saveResult)

        addToSearchQueue(post)
    }

    private fun addToSearchQueue(post: Post) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.DELETE_POST))
            .addData("id", post.id.toString())
            .build()

        searchQueueService.addQueueAsync(event)*/
    }
}
