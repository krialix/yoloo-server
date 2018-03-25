package com.yoloo.server.core.user.domain.objectify

import com.googlecode.objectify.impl.translate.TranslatorFactory
import com.yoloo.server.core.user.domain.model.User
import com.yoloo.server.core.user.domain.objectify.translate.LocaleTranslatorFactory
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import org.springframework.stereotype.Component

@Component
class UserObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerObjectifyTranslators(): List<TranslatorFactory<*, *>> {
        return listOf(LocaleTranslatorFactory())
    }

    override fun registerObjectifyEntities(): List<Class<*>> {
        return listOf(User::class.java)
    }
}