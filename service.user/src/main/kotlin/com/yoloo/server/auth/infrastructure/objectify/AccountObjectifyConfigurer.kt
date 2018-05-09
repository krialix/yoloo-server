package com.yoloo.server.auth.infrastructure.objectify

import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.auth.domain.entity.OauthUser
import org.springframework.stereotype.Component

@Component
class AccountObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerObjectifyEntities(): List<Class<*>> {
        return listOf(OauthUser::class.java)
    }
}