package com.yoloo.server.user.domain.objectify

import com.googlecode.objectify.impl.translate.TranslatorFactory
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.user.domain.model.User
import com.yoloo.server.user.domain.objectify.translators.CuckooFilterTranslatorFactory
import com.yoloo.server.user.domain.objectify.translators.LocaleTranslatorFactory
import org.springframework.stereotype.Component

@Component
class UserObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerObjectifyTranslators(): List<TranslatorFactory<*, *>> {
        return listOf(LocaleTranslatorFactory(), CuckooFilterTranslatorFactory())
    }

    override fun registerObjectifyEntities(): List<Class<*>> {
        return listOf(User::class.java)
    }
}