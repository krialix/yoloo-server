package com.yoloo.server.bookmark.config

import com.yoloo.server.bookmark.entity.Bookmark
import com.yoloo.server.objectify.ObjectifyConfigurer
import org.springframework.context.annotation.Configuration

@Configuration
class BookmarkObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Bookmark::class.java)
    }
}
