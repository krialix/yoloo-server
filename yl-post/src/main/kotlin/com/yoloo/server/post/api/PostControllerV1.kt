package com.yoloo.server.post.api

import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.post.usecase.GetPostUseCase
import com.yoloo.server.post.usecase.ListGroupFeedUseCase
import com.yoloo.server.post.vo.PostResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping(
    "/api/v1/posts",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PostControllerV1(
    private val getPostUseCase: GetPostUseCase,
    private val listGroupFeedUseCase: ListGroupFeedUseCase
) {

    @PreAuthorize("#oauth2.hasScope('post:read')")
    @GetMapping("/{postId}")
    @ResponseBody
    fun getPost(principal: Principal, @PathVariable("postId") postId: Long): PostResponse {
        return getPostUseCase.execute(GetPostUseCase.Params(principal, postId))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun insertPost(principal: Principal?) {
        /*(1..5).map {
            return@map when (it) {
                1, 2 -> Post(
                    author = Author(id = "id$it", displayName = "user$it", avatarUrl = ""),
                    data = TextPostData(
                        title = PostTitle("title-deneme-$it"),
                        group = PostGroup(id = "id$it", displayName = "group$it"),
                        tags = setOf(PostTag("tag"))
                    ),
                    content = PostContent("lorem impsum")
                )
                3 -> Post(
                    author = Author(id = "id$it", displayName = "user$it", avatarUrl = ""),
                    data = RichPostData(
                        title = PostTitle("title-deneme-$it"),
                        group = PostGroup(id = "id$it", displayName = "group$it"),
                        tags = setOf(PostTag("tag")),
                        attachments = listOf(
                            PostAttachment("/hello", "http://hello.jpg"),
                            PostAttachment("/hello", "http://hello.jpg")
                        )
                    ),
                    content = PostContent("lorem impsum")
                )
                4 -> Post(
                    author = Author(id = "id$it", displayName = "user$it", avatarUrl = ""),
                    data = SponsoredPostData(
                        title = PostTitle("title-deneme-$it"),
                        attachments = listOf(
                            PostAttachment("/hello", "http://hello.jpg"),
                            PostAttachment("/hello", "http://hello.jpg")
                        )
                    ),
                    content = PostContent("lorem impsum")

                )
            *//*5 -> Post(
                author = Author(id = "id$it", displayName = "user$it", avatarUrl = ""),
                data = BuddyPostData(
                    title = PostTitle("title-deneme-$it"),
                    group = PostGroup(id = "id$it", displayName = "group$it"),
                    tags = setOf(PostTag("tag")),
                    buddyRequestInfo = BuddyRequestInfo(
                        Range(1, 3),
                        Location(""),
                        Range(LocalDate.of(2018, Month.JUNE, 15), LocalDate.of(2018, Month.JUNE, 20))
                    )
                ),
                type = PostType.BUDDY,
                content = PostContent("lorem impsum")
            )*//*
                else -> Post(
                    author = Author(id = "id$it", displayName = "user$it", avatarUrl = ""),
                    data = TextPostData(
                        title = PostTitle("title-deneme-$it"),
                        group = PostGroup(id = "id$it", displayName = "group$it"),
                        tags = setOf(PostTag("tag"))
                    ),
                    content = PostContent("lorem impsum")
                )
            }
        }.let { ofy().save().entities(it) }*/
    }

    @GetMapping("/topics/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun listTopicPosts(
        principal: Principal?,
        @PathVariable("id") topicId: String,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<PostResponse> {
        return listGroupFeedUseCase.execute(ListGroupFeedUseCase.Request(principal, topicId, cursor))
    }
}