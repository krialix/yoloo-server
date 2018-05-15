package com.yoloo.server.post.infrastructure.objectify

import com.googlecode.objectify.impl.translate.TranslatorFactory
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.vo.postdata.*
import com.yoloo.server.post.infrastructure.objectify.translators.PostPermissionEnumTranslatorFactory
import org.springframework.stereotype.Component

@Component
class PostObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerTranslators(): List<TranslatorFactory<*, *>> {
        return listOf(PostPermissionEnumTranslatorFactory())
    }

    override fun registerEntities(): List<Class<*>> {
        return listOf(
            Post::class.java,
            PostData::class.java,
            TextPostData::class.java,
            RichPostData::class.java,
            SponsoredPostData::class.java,
            BuddyPostData::class.java
        )
    }
}