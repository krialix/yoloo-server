package com.yoloo.server.comment.infrastructure.objectify

import com.yoloo.server.comment.domain.entity.Comment
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import org.springframework.stereotype.Component

@Component
class CommentObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Comment::class.java)
    }
}