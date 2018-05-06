package com.yoloo.server.relationship.infrastructure.objectify

import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.relationship.domain.entity.Relationship
import org.springframework.stereotype.Component

@Component
class RelationshipObjectifyConfigurer : ObjectifyConfigurer {

    override fun registerObjectifyEntities(): List<Class<*>> {
        return listOf(Relationship::class.java)
    }
}