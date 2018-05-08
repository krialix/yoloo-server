package com.yoloo.server.auth.infrastructure.objectify

import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.auth.domain.entity.Account
import org.springframework.stereotype.Component

@Component
class AccountObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerObjectifyEntities(): List<Class<*>> {
        return listOf(Account::class.java)
    }
}