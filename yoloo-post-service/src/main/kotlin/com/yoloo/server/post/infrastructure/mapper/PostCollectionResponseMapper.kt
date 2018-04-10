package com.yoloo.server.post.infrastructure.mapper

import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.PostOwnerResponse
import com.yoloo.server.post.domain.response.PostResponse
import org.dialectic.jsonapi.response.DataResponse
import org.dialectic.jsonapi.response.JsonApi
import java.util.function.Function

class PostCollectionResponseMapper :
    Function<List<Post>, DataResponse<PostResponse>> {

    override fun apply(posts: List<Post>): DataResponse<PostResponse> {
        val data = mutableListOf<PostResponse>()
        val resources = mutableListOf<PostOwnerResponse>()

        posts.forEach {
            data.add(PostResponse.of(it))
            resources.add(PostOwnerResponse.of(it.owner, false))
        }

        return JsonApi.data(data).withIncludedResources(resources)
    }
}