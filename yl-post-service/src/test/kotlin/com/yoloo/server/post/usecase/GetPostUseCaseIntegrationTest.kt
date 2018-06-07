package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.common.truth.Truth.assertThat
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
import com.yoloo.server.rest.exception.NotFoundException
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPostUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    private val memcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getMemcacheService() }
    private val postResponseMapper by lazy(LazyThreadSafetyMode.NONE) { PostResponseMapper() }
    private val getPostUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetPostUseCase(
            postResponseMapper,
            memcacheService
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
    fun getPostById_postExists_ShouldReturnSamePost() {
        val original = ofy().saveClearLoad(createDemoPost())

        val postResponse = getPostUseCase.execute(1, 1)

        assertThat(postResponse.id).isEqualTo(original.id)
        assertThat(postResponse.author.id).isEqualTo(original.author.id)
        assertThat(postResponse.author.displayName).isEqualTo(original.author.displayName)
        assertThat(original.id).isEqualTo(postResponse.id)
        assertThat(original.id).isEqualTo(postResponse.id)
    }

    @Test(expected = NotFoundException::class)
    fun getPostById_postNotExists_ShouldReturnSamePost() {
        ofy().saveClearLoad(createDemoPost())

        getPostUseCase.execute(1, 10)
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