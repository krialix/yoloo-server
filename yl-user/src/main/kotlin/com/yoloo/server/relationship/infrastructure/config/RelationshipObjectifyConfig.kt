package com.yoloo.server.relationship.infrastructure.config

import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.relationship.domain.entity.Relationship
import org.springframework.stereotype.Component

@Component
class RelationshipObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Relationship::class.java)
    }
}