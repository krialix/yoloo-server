package com.yoloo.server.post.infrastructure.objectify.translators

import com.yoloo.server.objectify.translators.AbstractSimpleTranslatorFactory
import com.yoloo.server.post.domain.vo.PostPermission

class PostPermissionEnumTranslatorFactory :
    AbstractSimpleTranslatorFactory<PostPermission, Long>(PostPermission::class.java) {

    override fun createTranslator(): SimpleTranslator<PostPermission, Long> {
        return object : SimpleTranslator<PostPermission, Long> {
            override fun loadValue(datastoreValue: Long): PostPermission {
                return PostPermission.values()[datastoreValue.toInt()]
            }

            override fun saveValue(pojoValue: PostPermission): Long {
                return pojoValue.ordinal.toLong()
            }
        }
    }
}
