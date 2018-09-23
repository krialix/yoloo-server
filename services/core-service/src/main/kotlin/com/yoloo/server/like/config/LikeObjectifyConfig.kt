package com.yoloo.server.like.config

import com.yoloo.server.objectify.ObjectifyConfigurer
import com.yoloo.server.like.entity.Like
import org.springframework.context.annotation.Configuration

@Configuration
class LikeObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Like::class.java)
    }
}
