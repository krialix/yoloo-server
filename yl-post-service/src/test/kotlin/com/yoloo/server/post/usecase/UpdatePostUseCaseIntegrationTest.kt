package com.yoloo.server.post.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.common.truth.Truth
import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.TestObjectifyService.fact
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class UpdatePostUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    private val memcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getMemcacheService() }
    private val postResponseMapper by lazy(LazyThreadSafetyMode.NONE) { PostResponseMapper() }
    private val objectMapper by lazy(LazyThreadSafetyMode.NONE) { ObjectMapper() }
    private val updatePostUseCase by lazy(LazyThreadSafetyMode.NONE) {
        UpdatePostUseCase(
            postResponseMapper,
            memcacheService,
            objectMapper,
            PubSubTemplate(null, null)
        )
    }

    @Before
    fun setUp() {
        memcacheService.put(Vote.KEY_FILTER_VOTE, NanoCuckooFilter.Builder(32).build())
        memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, NanoCuckooFilter.Builder(32).build())

        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Post::class.java)
    }

    @Test
    fun getPostById_postExistsAndChangeContent_ShouldReturnUpdatedPost() {
        ofy().saveClearLoad(createDemoPost())

        val postResponse = updatePostUseCase.execute(2, 1, UpdatePostRequest("changed"))

        Truth.assertThat(postResponse.content).isEqualTo("changed")
    }

    private fun createDemoPost(): Post {
        return Post(
            id = 1,
            type = PostType.TEXT,
            author = Author(
                id = 2,
                displayName = "demo author",
                avatar = AvatarImage(Url("urlLink")),
                verified = false
            ),
            title = PostTitle("lorem impsum title"),
            content = PostContent("lorem impsum content"),
            group = PostGroup(
                id = 3,
                displayName = "group1"
            ),
            tags = setOf(PostTag("tag1"), PostTag("tag2"))
        )
    }
}