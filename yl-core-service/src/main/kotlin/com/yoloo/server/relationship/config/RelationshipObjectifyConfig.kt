package com.yoloo.server.relationship.config

import com.yoloo.server.objectify.ObjectifyConfigurer
import com.yoloo.server.relationship.entity.Relationship
import org.springframework.stereotype.Component

@Component
class RelationshipObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Relationship::class.java)
    }
}
