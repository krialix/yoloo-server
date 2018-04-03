package com.yoloo.server.post.domain.response

import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("user")
data class PostOwnerResponse(@Id val id: String, val displayName: String, val avatarUrl: String, val self: Boolean)