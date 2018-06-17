package com.yoloo.server.post.usecase

import com.yoloo.server.common.event.PubSubEvent
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.PostTag
import com.yoloo.server.post.vo.UpdatePostRequest
import org.springframework.context.ApplicationEventPublisher

class UpdatePostUseCase(private val eventPublisher: ApplicationEventPublisher) {

    fun execute(requesterId: Long, postId: Long, request: UpdatePostRequest) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")

        val self = requesterId == post.author.id

        ServiceExceptions.checkForbidden(self, "post.forbidden_update")

        request.content?.let { post.content.value = it }
        request.tags?.map { PostTag(it) }?.toSet()?.let { post.tags = it }

        val saveResult = ofy().save().entity(post)
        TestUtil.saveResultsNowIfTest(saveResult)

        eventPublisher.publishEvent(PubSubEvent("post.update", post, this))
    }
}