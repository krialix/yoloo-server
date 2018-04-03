package com.yoloo.server.post.domain.response

import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type

@Type("post")
data class PostResponse(
    @Id
    val id: String,

    @Relationship("user")
    val owner: PostOwnerResponse,

    val title: String/*,

    val content: String,

    val createdAt: LocalDateTime*/
)