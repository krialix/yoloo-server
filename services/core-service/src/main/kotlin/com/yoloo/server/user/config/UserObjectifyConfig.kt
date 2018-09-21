package com.yoloo.server.user.config

import com.yoloo.server.objectify.ObjectifyConfigurer
import com.yoloo.server.user.entity.User
import org.springframework.stereotype.Component

@Component
class UserObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(User::class.java)
    }
}
