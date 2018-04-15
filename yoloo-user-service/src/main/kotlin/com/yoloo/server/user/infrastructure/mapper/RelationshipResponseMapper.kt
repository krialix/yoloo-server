package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.Relationship
import com.yoloo.server.user.domain.response.RelationshipResponse
import org.springframework.stereotype.Component

@Component
class RelationshipResponseMapper : Mapper<Relationship, RelationshipResponse> {

    override fun apply(relationship: Relationship): RelationshipResponse {
        return RelationshipResponse(
            id = relationship.id,
            displayName = relationship.displayName.value,
            avatarUrl = relationship.avatarUrl
        )
    }
}