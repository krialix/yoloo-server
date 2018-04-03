package com.yoloo.server.post.domain.response

import com.jianglibo.tojsonapi.reflect.JsonapiRelation
import com.jianglibo.tojsonapi.reflect.JsonapiResource

@JsonapiResource(type = "posts")
data class PostResponse(
    var id: String,

    @JsonapiRelation(
        targetResourceClass = PostOwnerResponse::class,
        relationType = JsonapiRelation.JsonapiRelationType.SINGLE
    )
    var owner: PostOwnerResponse,

    var title: String/*,

    val content: String,

    val createdAt: LocalDateTime*/
)