package com.yoloo.server.post.usecase

import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.util.PostErrors
import org.springframework.stereotype.Service

@Service
class DeletePostUseCase {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().key(Post.createKey(postId)).now()

        ServiceExceptions.checkNotFound(post != null, PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkForbidden(post.author.id == requesterId, PostErrors.ERROR_POST_FORBIDDEN)

        post.markAsDeleted()

        ofy().save().entity(post)

        addToSearchQueue(post)
    }

    private fun addToSearchQueue(post: Post) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.DELETE_POST))
            .addData("id", post.id.toString())
            .build()

        searchQueueService.addQueueAsync(event)*/
    }
}
