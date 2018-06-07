package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.rest.exception.ServiceExceptions
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import java.time.LocalDateTime

class DeletePostUseCase(private val pubSubTemplate: PubSubTemplate, private val objectMapper: ObjectMapper) {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden")

        post.auditData.deletedAt = LocalDateTime.now()

        ofy().save().entity(post)

        publishPostDeletedEvent(post)
    }

    private fun publishPostDeletedEvent(post: Post) {
        val json = objectMapper.writeValueAsString(post)
        pubSubTemplate.publish("post.delete", json, null)
    }
}