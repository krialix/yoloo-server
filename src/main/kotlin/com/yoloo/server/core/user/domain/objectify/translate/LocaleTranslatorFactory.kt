package com.yoloo.server.core.user.domain.objectify.translate

import com.googlecode.objectify.impl.Path
import com.googlecode.objectify.impl.translate.*
import java.util.*

/**
 * Create a new instance.
 */
class LocaleTranslatorFactory : ValueTranslatorFactory<Locale, String>(Locale::class.java) {

    override fun createValueTranslator(
        tk: TypeKey<Locale>,
        ctx: CreateContext,
        path: Path
    ): ValueTranslator<Locale, String> {
        return object : ValueTranslator<Locale, String>(String::class.java) {
            @Throws(SkipException::class)
            override fun loadValue(value: String, ctx: LoadContext, path: Path): Locale {
                return Locale.forLanguageTag(value)
            }

            @Throws(SkipException::class)
            override fun saveValue(value: Locale, index: Boolean, ctx: SaveContext, path: Path): String {
                return value.toLanguageTag()
            }
        }
    }
}
