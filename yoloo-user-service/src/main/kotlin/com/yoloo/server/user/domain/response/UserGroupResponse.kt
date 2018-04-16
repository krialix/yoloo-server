package com.yoloo.server.user.domain.response

import org.dialectic.jsonapi.resource.Resource

data class UserGroupResponse(val id: String, val displayName: String, val imageUrl: String) : Resource {

    override fun getJsonApiDataId(): String {
        return id
    }

    override fun getJsonApiDataType(): String {
        return "groups"
    }
}