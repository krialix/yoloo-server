package com.yoloo.server.user.infrastructure.objectify.translators

import com.yoloo.server.objectify.translators.AbstractSimpleTranslatorFactory
import java.util.*

class LocaleTranslatorFactory : AbstractSimpleTranslatorFactory<Locale, String>(Locale::class.java) {

    override fun createTranslator(): SimpleTranslator<Locale, String> {
        return object : SimpleTranslator<Locale, String> {
            override fun loadValue(datastoreValue: String?): Locale {
                return Locale.forLanguageTag(datastoreValue)
            }

            override fun saveValue(pojoValue: Locale?): String {
                return pojoValue.toString()
            }
        }
    }
}
