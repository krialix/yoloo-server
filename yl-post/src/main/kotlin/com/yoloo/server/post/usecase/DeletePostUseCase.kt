package com.yoloo.server.post.usecase

import com.yoloo.server.api.exception.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.entity.Post
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Lazy
@Component
class DeletePostUseCase {
    fun execute(requesterId: Long, postId: Long) {
        var post = ofy().load().type(Post::class.java).id(postId).now()

        ServiceExceptions.checkNotFound(post != null, "post.not_found")
        ServiceExceptions.checkNotFound(!post.isDeleted(), "post.not_found")
        ServiceExceptions.checkForbidden(post.author.id == requesterId, "forbidden")

        post = post.copy(deletedAt = LocalDateTime.now())

        ofy().save().entity(post)
        // TODO remove title & tags from search service
        // TODO recreate vote filter
    }
}