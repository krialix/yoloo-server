package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.common.collect.Iterables
import com.google.common.truth.Truth.assertThat
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.TestObjectifyService.fact
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.vo.*
import com.yoloo.server.post.vo.postdataresponse.TextPostDataResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListGroupFeedUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    private val memcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getMemcacheService() }
    private val asyncMemcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getAsyncMemcacheService() }
    private val postResponseMapper by lazy(LazyThreadSafetyMode.NONE) { PostResponseMapper() }
    private val listGroupFeedUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ListGroupFeedUseCase(postResponseMapper, memcacheService)
    }
    private val postResponseComparator by lazy(LazyThreadSafetyMode.NONE) {
        Comparator<PostResponse> { p1, p2 -> (p2.id - p1.id).toInt() }
    }
    private val votePostUseCase by lazy(LazyThreadSafetyMode.NONE) { VotePostUseCase(asyncMemcacheService) }

    @Before
    fun setUp() {
        memcacheService.put(Filters.KEY_FILTER_VOTE, NanoCuckooFilter.Builder(32).build())

        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Post::class.java)
        fact().register(Vote::class.java)
    }

    @Test
    fun listGroupFeed_whenFeedIsEmpty_shouldReturnEmpty() {
        val collectionResponse = listGroupFeedUseCase.execute(100, 100, null)

        assertThat(collectionResponse.data).isEmpty()
        assertThat(collectionResponse.prevPageToken).isNull()
        assertThat(collectionResponse.nextPageToken).isNull()
    }

    @Test
    fun listGroupFeed_whenFeedIsNotEmpty_shouldReturnAllResponse() {
        saveExamplePosts(200L)

        val collectionResponse = listGroupFeedUseCase.execute(100, 200, null)

        assertThat(collectionResponse.data).hasSize(10)
        assertThat(collectionResponse.data).isOrdered(postResponseComparator)
        assertThat(collectionResponse.prevPageToken).isNull()
        assertThat(collectionResponse.nextPageToken).isNotNull()
    }

    @Test
    fun listGroupFeed_whenGivenCursor_shouldReturnPaginatedResponse() {
        saveExamplePosts(200L)

        val collectionResponse1 = listGroupFeedUseCase.execute(100, 200, null)

        val collectionResponse2 = listGroupFeedUseCase.execute(100, 200, collectionResponse1.nextPageToken)

        assertThat(collectionResponse2.data).hasSize(10)
        assertThat(collectionResponse2.data).isOrdered(postResponseComparator)
        assertThat(collectionResponse2.prevPageToken).isNotNull()
        assertThat(collectionResponse2.nextPageToken).isNotNull()
    }

    @Test
    fun listGroupFeed_whenFeedIsNotEmptyAndVoted_shouldReturnVoted() {
        saveExamplePosts(200L)
        votePostUseCase.execute(100, 60)

        val collectionResponse = listGroupFeedUseCase.execute(100, 200, null)

        val votedPostDataResponse = Iterables.get(collectionResponse.data, 0).data as TextPostDataResponse

        assertThat(votedPostDataResponse.voted).isTrue()
        assertThat(collectionResponse.data).hasSize(50)
        assertThat(collectionResponse.data).isOrdered(postResponseComparator)
        assertThat(collectionResponse.prevPageToken).isNull()
        assertThat(collectionResponse.nextPageToken).isNotNull()
    }

    private fun saveExamplePosts(groupId: Long): Map<Key<Post>, Post> {
        return (1..60)
            .map {
                Post(
                    id = it.toLong(),
                    type = PostType.TEXT,
                    author = Author(
                        id = it.toLong(),
                        displayName = "user_$it",
                        avatar = AvatarImage(Url("")),
                        verified = false
                    ),
                    title = PostTitle("title_$it"),
                    content = PostContent("content_$it"),
                    group = PostGroup(groupId, "group"),
                    tags = emptySet()
                )
            }.let { return@let ofy().save().entities(it).now() }
    }
}