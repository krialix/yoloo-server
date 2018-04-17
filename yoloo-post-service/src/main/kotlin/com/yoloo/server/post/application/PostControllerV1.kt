package com.yoloo.server.post.application

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.PostResponse
import com.yoloo.server.post.domain.usecase.GetPostUseCase
import com.yoloo.server.post.domain.usecase.contract.GetPostUseCaseContract
import com.yoloo.server.post.domain.vo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping(
    "/api/v1/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostControllerV1 @Autowired constructor(val getPostUseCase: GetPostUseCase) {

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getPost(principal: Principal?, @PathVariable("postId") postId: String): PostResponse {
        return getPostUseCase.execute(GetPostUseCaseContract.Request(principal, postId)).response
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun insertPost(principal: Principal?) {
        (1..5).map {
            Post(
                author = Author(id = "id$it", displayName = "user$it", avatarUrl = ""),
                title = PostTitle("title-deneme-$it"),
                topic = PostTopic(topicId = "topicId$it", displayName = "topic$it"),
                tags = setOf(PostTag("tag")),
                type = PostType.TEXT,
                content = PostContent("lorem impsum")
            )
        }.let { ofy().save().entities(it) }
    }
}