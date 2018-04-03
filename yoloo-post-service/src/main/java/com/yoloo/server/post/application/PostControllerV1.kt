package com.yoloo.server.post.application

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.PostOwnerResponse
import com.yoloo.server.post.domain.response.PostResponse
import com.yoloo.server.post.domain.vo.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController



@RequestMapping("/api/v1")
@RestController
class PostControllerV1 {

    @GetMapping("/posts")
    fun listPosts(): JSONAPIDocument<PostResponse> {
        val list = ofy().load().type(Post::class.java)
            .limit(2)
            .list()
            .asSequence()
            .map {
                PostResponse(
                    id = it.id,
                    owner = PostOwnerResponse(
                        id = it.owner.userId,
                        displayName = it.owner.displayName,
                        avatarUrl = it.owner.avatarUrl,
                        self = false
                    ),
                    title = it.title.value
                )
            }.toList()

        val converter = ResourceConverter(PostResponse::class.java, PostOwnerResponse::class.java)
        val bytes = converter.writeDocumentCollection(JSONAPIDocument(list))
        return converter.readDocument(bytes, PostResponse::class.java)
    }

    @GetMapping("/posts2")
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
}