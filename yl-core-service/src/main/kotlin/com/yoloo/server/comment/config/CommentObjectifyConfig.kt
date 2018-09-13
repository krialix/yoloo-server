package com.yoloo.server.comment.config

import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.objectify.ObjectifyConfigurer
import org.springframework.context.annotation.Configuration

@Configuration
class CommentObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Comment::class.java)
    }
}
