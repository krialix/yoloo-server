package com.yoloo.server.post.usecase

import com.google.common.truth.Truth.assertThat
import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.TestObjectifyService.fact
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPostUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    @Before
    fun setUp() {
        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Post::class.java)
    }

    @Test
    fun getPostById_validId_ShouldReturnSamePost() {
        val original = createDemoPost()

        val fetched = ofy().saveClearLoad(original)

        assertThat(fetched).isEqualTo(original)
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