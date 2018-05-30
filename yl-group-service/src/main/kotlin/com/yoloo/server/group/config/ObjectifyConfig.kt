package com.yoloo.server.group.config

import com.yoloo.server.group.entity.Group
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import org.springframework.stereotype.Component

@Component
class ObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Group::class.java)
    }
}