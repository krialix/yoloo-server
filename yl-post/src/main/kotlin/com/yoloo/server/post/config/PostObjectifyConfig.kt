package com.yoloo.server.post.config

import com.googlecode.objectify.impl.translate.TranslatorFactory
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.postdata.*
import com.yoloo.server.post.config.translators.PostAclTranslatorFactory
import org.springframework.stereotype.Component

@Component
class PostObjectifyConfig : ObjectifyConfigurer {

    override fun registerTranslators(): List<TranslatorFactory<*, *>> {
        return listOf(PostAclTranslatorFactory())
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