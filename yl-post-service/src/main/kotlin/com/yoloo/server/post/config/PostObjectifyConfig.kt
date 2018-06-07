package com.yoloo.server.post.config

import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import org.springframework.stereotype.Component

@Component
class PostObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Post::class.java, Vote::class.java, Bookmark::class.java, Comment::class.java)
    }
}