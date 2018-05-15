package com.yoloo.server.relationship.infrastructure.mapper

import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class RelationshipResponseMapper : Function<Relationship, RelationshipResponse> {

    override fun apply(from: Relationship): RelationshipResponse {
        return RelationshipResponse(
            id = from.id,
            displayName = from.displayName.value,
            avatarUrl = from.avatarImage.url.value
        )
    }
}