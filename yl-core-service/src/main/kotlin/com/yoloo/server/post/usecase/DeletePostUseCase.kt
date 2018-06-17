package com.yoloo.server.post.usecase

import com.yoloo.server.common.event.PubSubEvent
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.context.ApplicationEventPublisher

class DeletePostUseCase(private val eventPublisher: ApplicationEventPublisher) {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden")

        post.markAsDeleted()

        val saveResult = ofy().save().entity(post)
        TestUtil.saveResultsNowIfTest(saveResult)

        eventPublisher.publishEvent(PubSubEvent("post.delete", post, this))
    }
}