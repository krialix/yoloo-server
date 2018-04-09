package com.yoloo.server.post.application

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.request.PostRequest
import com.yoloo.server.post.domain.response.PostOwnerResponse
import com.yoloo.server.post.domain.response.PostResponse
import com.yoloo.server.post.domain.vo.*
import org.dialectic.jsonapi.response.DataResponse
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1", produces = ["application/vnd.api+json"], consumes = ["application/vnd.api+json"])
@RestController
class PostControllerV1 {

    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    fun getPost(@PathVariable("postId") postId: String): DataResponse<PostResponse> {
        val post = ofy().load().type(Post::class.java).id(postId).now()

        val postResponse = PostResponse(
            id = post.id,
            ownerId = post.owner.userId,
            title = post.title.value,
            content = post.content.value,
            createdAt = post.createdAt
        )

        val postOwnerResponse = PostOwnerResponse(
            id = post.owner.userId,
            displayName = post.owner.displayName,
            avatarUrl = post.owner.avatarUrl,
            self = false
        )

        return JsonApi.data(postResponse).withIncludedResources(postOwnerResponse)
    }

    @GetMapping("/posts")
    fun listPosts(): DataResponse<PostResponse> {
        val postOwnerResponses = mutableListOf<PostOwnerResponse>()
        val postResponses = mutableListOf<PostResponse>()

        ofy().load().type(Post::class.java)
            .limit(2)
            .list()
            .forEach {
                val postResponse = PostResponse(
                    id = it.id,
                    ownerId = it.owner.userId,
                    title = it.title.value,
                    content = it.content.value,
                    createdAt = it.createdAt
                )
                postResponses.add(postResponse)

                val postOwnerResponse = PostOwnerResponse(
                    id = it.owner.userId,
                    displayName = it.owner.displayName,
                    avatarUrl = it.owner.avatarUrl,
                    self = false
                )
                postOwnerResponses.add(postOwnerResponse)
            }

        return JsonApi.data(postResponses).withIncludedResources(postOwnerResponses)
    }

    @PostMapping("/posts")
    fun insertPost() {
        (1..5).map {
            Post(
                owner = PostOwner(userId = "userId$it", displayName = "user$it", avatarUrl = ""),
                title = PostTitle("title$it"),
                topic = PostTopic(topicId = "topicId$it", displayName = "topic$it"),
                tags = setOf(PostTag("tag")),
                type = PostType.TEXT,
                content = PostContent.create("lorem impsum")
            )
        }.let { ofy().save().entities(it).now() }
    }

    @PostMapping("/posts5")
    fun insertPost(@Valid @RequestBody postRequest: PostRequest) {

    }
}