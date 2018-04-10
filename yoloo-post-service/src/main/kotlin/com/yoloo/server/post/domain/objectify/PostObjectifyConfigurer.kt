package com.yoloo.server.post.domain.objectify

import com.googlecode.objectify.impl.translate.TranslatorFactory
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.objectify.translators.PostPermissionEnumTranslatorFactory
import org.springframework.stereotype.Component

@Component
class PostObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerObjectifyTranslators(): List<TranslatorFactory<*, *>> {
        return listOf(PostPermissionEnumTranslatorFactory())
    }

    override fun registerObjectifyEntities(): List<Class<*>> {
        return listOf(Post::class.java)
    }
}