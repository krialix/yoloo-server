package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.common.truth.Truth.assertThat
import com.yoloo.server.rest.error.exception.ForbiddenException
import com.yoloo.server.rest.error.exception.NotFoundException
import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.TestObjectifyService.fact
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class VotePostUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    private val memcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getAsyncMemcacheService() }
    private val votePostUseCase by lazy(LazyThreadSafetyMode.NONE) { VotePostUseCase(memcacheService) }

    @Before
    fun setUp() {
        memcacheService.put(Vote.KEY_FILTER_VOTE, NanoCuckooFilter.Builder(32).build()).get()
        memcacheService.put(Bookmark.KEY_FILTER_BOOKMARK, NanoCuckooFilter.Builder(32).build()).get()

        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Post::class.java)
        fact().register(Vote::class.java)
    }

    @Test
    fun votePost_whenPostIsExists_shouldVote() {
        val userId = 100L

        val post = ofy().saveClearLoad(createVotingAllowedPost())

        votePostUseCase.execute(userId, post.id)

        val voteKey = Vote.createKey(userId, post.id, "p")

        val map = ofy().load().keys(post.key, voteKey) as Map<*, *>
        val fetchedPost = map[post.key] as Post
        val fetchedVote = map[voteKey] as Vote?

        assertThat(fetchedPost.countData.voteCount).isEqualTo(1)
        assertThat(fetchedVote).isNotNull()
        assertThat(fetchedVote).isEqualTo(Vote(Vote.createId(userId, post.id, "p"), 1))

        val voteFilter = memcacheService.get(Vote.KEY_FILTER_VOTE).get() as NanoCuckooFilter
        assertThat(voteFilter.contains(voteKey.name)).isTrue()
    }

    @Test(expected = NotFoundException::class)
    fun votePost_whenPostIsNotExists_shouldThrowNotFoundException() {
        val userId = 100L

        votePostUseCase.execute(userId, 1000)
    }

    @Test(expected = ForbiddenException::class)
    fun votePost_whenPostVotingDisabled_shouldNotVote() {
        val post = ofy().saveClearLoad(createVotingDisabledPost())

        votePostUseCase.execute(100L, post.id)
    }

    private fun createVotingAllowedPost(): Post {
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

    private fun createVotingDisabledPost(): Post {
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
            flags = EnumSet.of(PostPermFlag.DISABLE_VOTING),
            tags = setOf(PostTag("tag1"), PostTag("tag2"))
        )
    }
}