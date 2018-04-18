package com.yoloo.server.relationship.infrastructure.mapper

import com.yoloo.server.common.util.Mapper
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import org.springframework.stereotype.Component

@Component
class RelationshipResponseMapper : Mapper<Relationship, RelationshipResponse> {

    override fun apply(from: Relationship, payload: MutableMap<String, Any>): RelationshipResponse {
        return RelationshipResponse(
            id = from.id,
            displayName = from.displayName.value,
            avatarUrl = from.avatarImage.value
        )
    }
}