package com.yoloo.server.group.config

import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.objectify.ObjectifyConfigurer
import org.springframework.stereotype.Component

@Component
class GroupObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Group::class.java, Subscription::class.java)
    }
}
