package com.yoloo.server.post.config.translators

import com.yoloo.server.objectify.translators.AbstractSimpleTranslatorFactory
import com.yoloo.server.post.vo.PostAcl

class PostAclTranslatorFactory : AbstractSimpleTranslatorFactory<PostAcl, Long>(PostAcl::class.java) {

    override fun createTranslator(): SimpleTranslator<PostAcl, Long> {
        return object : SimpleTranslator<PostAcl, Long> {
            override fun loadValue(datastoreValue: Long): PostAcl {
                return PostAcl.values()[datastoreValue.toInt()]
            }

            override fun saveValue(pojoValue: PostAcl): Long {
                return pojoValue.ordinal.toLong()
            }
        }
    }
}
