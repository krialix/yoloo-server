package com.yoloo.server.post.domain.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.yoloo.server.post.domain.vo.PostOwner
import org.dialectic.jsonapi.resource.Resource

data class PostOwnerResponse(
    @JsonIgnore
    var id: String,
    var displayName: String,
    var avatarUrl: String,
    var self: Boolean
) : Resource {
    override fun getJsonApiDataId(): String {
        return id
    }

    override fun getJsonApiDataType(): String {
        return "users"
    }

    companion object {
        fun of(owner: PostOwner, self: Boolean): PostOwnerResponse {
            return PostOwnerResponse(
                id = owner.userId,
                displayName = owner.displayName,
                avatarUrl = owner.avatarUrl,
                self = self
            )
        }
    }
}