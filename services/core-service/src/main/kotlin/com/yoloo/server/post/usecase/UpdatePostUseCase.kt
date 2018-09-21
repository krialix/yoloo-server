package com.yoloo.server.post.usecase

import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.UpdatePostRequest
import org.springframework.stereotype.Service

@Service
class UpdatePostUseCase {

    fun execute(requesterId: Long, postId: Long, request: UpdatePostRequest) {
        val post = ofy().load().key(Post.createKey(postId)).now()

        ServiceExceptions.checkNotFound(post != null, PostErrors.ERROR_POST_NOT_FOUND)
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, PostErrors.ERROR_POST_NOT_FOUND)

        val self = requesterId == post.author.id

        ServiceExceptions.checkForbidden(self, PostErrors.ERROR_POST_FORBIDDEN)

        request.content?.let { post.content.value = it }
        request.tags?.toSet()?.let { post.tags = it }

        ofy().save().entity(post)

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
