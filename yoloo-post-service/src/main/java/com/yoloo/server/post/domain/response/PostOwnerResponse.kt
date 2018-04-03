package com.yoloo.server.post.domain.response

import com.jianglibo.tojsonapi.reflect.JsonapiResource

@JsonapiResource(type = "user")
data class PostOwnerResponse(var id: String, var displayName: String, var avatarUrl: String, var self: Boolean)