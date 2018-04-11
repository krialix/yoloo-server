package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg
import org.dialectic.jsonapi.resource.Resource

@NoArg
data class UserResponse(
    val id: String,
    val displayName: String,
    val self: Boolean,
    val following: Boolean,
    val avatarUrl: String,
    val email: String,
    val bio: String?,
    val website: String?,
    val count: UserCountResponse,
    val countryIsoCode: String
) : Resource {
    override fun getJsonApiDataId(): String {
        return id
    }

    override fun getJsonApiDataType(): String {
        return "user"
    }
}