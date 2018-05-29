package com.yoloo.server.post.usecase

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import com.yoloo.server.rest.error.exception.ServiceExceptions
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Lazy
@Component
class DeletePostUseCase {

    fun execute(requesterId: Long, postId: Long) {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.auditData.isDeleted, "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "post.forbidden")

        post.auditData.deletedAt = LocalDateTime.now()

        ofy().save().entity(post)
        // TODO remove title & tags from search service
        // TODO recreate vote filter
    }
}