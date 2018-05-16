package com.yoloo.server.post.config.translators

import com.yoloo.server.objectify.translators.AbstractSimpleTranslatorFactory
import com.yoloo.server.post.vo.PostPermFlag

class EnumTranslatorFactory : AbstractSimpleTranslatorFactory<PostPermFlag, Long>(PostPermFlag::class.java) {

    override fun createTranslator(): SimpleTranslator<PostPermFlag, Long> {
        return object : SimpleTranslator<PostPermFlag, Long> {
            override fun loadValue(datastoreValue: Long): PostPermFlag {
                return PostPermFlag.values()[datastoreValue.toInt()]
            }

            override fun saveValue(pojoValue: PostPermFlag): Long {
                return pojoValue.ordinal.toLong()
            }
        }
    }
}
