package com.yoloo.server.post.domain.response

import com.fasterxml.jackson.annotation.JsonIgnore
import org.dialectic.jsonapi.ResourceLinkage
import org.dialectic.jsonapi.relationship.Relationship
import org.dialectic.jsonapi.relationship.Relationships
import org.dialectic.jsonapi.resource.Resource
import java.time.LocalDateTime

data class PostResponse(
    @JsonIgnore
    var id: String,

    @JsonIgnore
    var ownerId: String,

    var title: String,

    val content: String,

    val createdAt: LocalDateTime
) : Resource {
    override fun getJsonApiDataId(): String {
        return id
    }

    override fun getJsonApiDataType(): String {
        return "posts"
    }

    override fun getRelationships(): Relationships {
        return Relationships.of(Relationship.create("owner", ResourceLinkage.toOne(ownerId, "users")))
    }
}