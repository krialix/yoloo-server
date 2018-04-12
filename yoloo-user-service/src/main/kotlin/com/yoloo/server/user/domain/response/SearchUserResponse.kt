package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg
import org.dialectic.jsonapi.resource.Resource

@NoArg
data class SearchUserResponse(
    val id: String,
    val displayName: String,
    val avatarUrl: String
) : Resource {
    override fun getJsonApiDataId(): String {
        return id
    }

    override fun getJsonApiDataType(): String {
        return "user"
    }
}