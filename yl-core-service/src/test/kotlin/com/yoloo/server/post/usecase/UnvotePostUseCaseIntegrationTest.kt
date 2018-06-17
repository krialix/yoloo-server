package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.common.truth.Truth.assertThat
import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.TestObjectifyService.fact
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.post.entity.Post
import com.yoloo.server.vote.entity.Vote
import com.yoloo.server.post.vo.*
import com.yoloo.server.common.exception.exception.NotFoundException
import com.yoloo.server.vote.usecase.UnvotePostUseCase
import com.yoloo.server.vote.usecase.VotePostUseCase
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UnvotePostUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    private val memcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getAsyncMemcacheService() }
    private val votePostUseCase by lazy(LazyThreadSafetyMode.NONE) {
        VotePostUseCase(
            memcacheService
        )
    }
    private val unvotePostUseCase by lazy(LazyThreadSafetyMode.NONE) {
        UnvotePostUseCase(
            memcacheService
        )
    }

    @Before
    fun setUp() {
        memcacheService.put(Vote.KEY_FILTER_VOTE, NanoCuckooFilter.Builder(32).build()).get()

        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Post::class.java)
        fact().register(Vote::class.java)
    }

    @Test
    fun unvotePost_whenPostIsExistsAndVotedBefore_shouldUnvote() {
        val userId = 100L

        val post = ofy().saveClearLoad(createVotingAllowedPost())
        val voteKey = Vote.createKey(userId, post.id, "p")

        votePostUseCase.execute(userId, post.id)

        unvotePostUseCase.execute(userId, post.id)

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        assertThat(voteFilter.contains(voteKey.name)).isFalse()

        val map = ofy().load().keys(post.key, voteKey) as Map<*, *>
        val unvotedPost = map[post.key] as Post
        val deletedVote = map[voteKey] as Vote?

        assertThat(unvotedPost.countData.voteCount).isEqualTo(0)
        assertThat(deletedVote).isNull()
    }

    @Test(expected = NotFoundException::class)
    fun unvotePost_whenPostIsNotExists_shouldThrowNotFoundException() {
        val userId = 100L

        unvotePostUseCase.execute(userId, 1000)
    }

    private fun createVotingAllowedPost(): Post {
        return Post(
            id = 1,
            type = PostType.TEXT,
            author = Author(
                id = 2,
                displayName = "demo author",
                avatar = AvatarImage(Url("urlLink"))
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