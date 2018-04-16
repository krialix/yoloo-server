package com.yoloo.server.relationship.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import org.springframework.stereotype.Component

@Component
class RelationshipResponseMapper : Mapper<Relationship, RelationshipResponse> {

    override fun apply(relationship: Relationship): RelationshipResponse {
        return RelationshipResponse(
            id = relationship.id,
            displayName = relationship.displayName.value,
            avatarUrl = relationship.avatarUrl.value
        )
    }
}