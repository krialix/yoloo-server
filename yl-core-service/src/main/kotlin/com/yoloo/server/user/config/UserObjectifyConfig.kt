package com.yoloo.server.user.config

import com.googlecode.objectify.impl.translate.TranslatorFactory
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.translator.CuckooFilterTranslatorFactory
import org.springframework.stereotype.Component

@Component
class UserObjectifyConfig : ObjectifyConfigurer {

    override fun registerTranslators(): List<TranslatorFactory<*, *>> {
        return listOf(CuckooFilterTranslatorFactory())
    }

    override fun registerEntities(): List<Class<*>> {
        return listOf(User::class.java)
    }
}