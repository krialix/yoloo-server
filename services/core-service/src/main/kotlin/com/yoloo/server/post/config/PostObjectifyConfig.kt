package com.yoloo.server.post.config

import com.yoloo.server.objectify.ObjectifyConfigurer
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import org.springframework.context.annotation.Configuration

@Configuration
class PostObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Post::class.java, Comment::class.java, Bookmark::class.java)
    }
}
