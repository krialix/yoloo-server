package com.yoloo.server.post.usecase

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.common.truth.Truth.assertThat
import com.googlecode.objectify.Key
import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.TestObjectifyService.fact
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.entity.Vote
import com.yoloo.server.post.mapper.CommentResponseMapper
import com.yoloo.server.post.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListCommentsUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    private val memcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getMemcacheService() }
    private val asyncMemcacheService by lazy(LazyThreadSafetyMode.NONE) { MemcacheServiceFactory.getAsyncMemcacheService() }
    private val commentResponseMapper by lazy(LazyThreadSafetyMode.NONE) { CommentResponseMapper() }
    private val listCommentsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ListCommentsUseCase(commentResponseMapper, memcacheService)
    }
    private val commentResponseComparator by lazy(LazyThreadSafetyMode.NONE) {
        Comparator<CommentResponse> { p1, p2 -> (p2.id - p1.id).toInt() }
    }
    private val approveCommentsUseCase by lazy(LazyThreadSafetyMode.NONE) { ApproveCommentUseCase() }

    @Before
    fun setUp() {
        memcacheService.put(Vote.KEY_FILTER_VOTE, NanoCuckooFilter.Builder(32).build())

        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Post::class.java)
        fact().register(Vote::class.java)
        fact().register(Comment::class.java)
    }

    @Test
    fun listComments_whenCommentsAreEmpty_shouldReturnEmpty() {
        createPost()

        val collectionResponse = listCommentsUseCase.execute(100, 1, null)

        assertThat(collectionResponse.data).isEmpty()
        assertThat(collectionResponse.prevPageToken).isNull()
        assertThat(collectionResponse.nextPageToken).isNull()
    }

    @Test
    fun listComments_whenCommentsAreNotEmpty_shouldReturnAllResponse() {
        createPost()
        createComments(1)

        val collectionResponse = listCommentsUseCase.execute(100, 1, null)

        assertThat(collectionResponse.approvedComment).isNull()
        assertThat(collectionResponse.data).hasSize(50)
        assertThat(collectionResponse.data).isOrdered(commentResponseComparator)
        assertThat(collectionResponse.prevPageToken).isNull()
        assertThat(collectionResponse.nextPageToken).isNotNull()
    }

    @Test
    fun listComments_whenCommentsAreNotEmptyAndCommentApproved_shouldReturnAllResponse() {
        createPost()
        createComments(1)
        approveCommentsUseCase.execute(2, 45)

        val collectionResponse = listCommentsUseCase.execute(100, 1, null)

        assertThat(collectionResponse.approvedComment.id).isEqualTo(45)
        assertThat(collectionResponse.data).hasSize(49)
        assertThat(collectionResponse.data).isOrdered(commentResponseComparator)
        assertThat(collectionResponse.prevPageToken).isNull()
        assertThat(collectionResponse.nextPageToken).isNotNull()
    }

    private fun createPost(): Post {
        val post = Post(
            id = 1,
            type = PostType.TEXT,
            author = Author(
                id = 2,
                displayName = "stub name",
                avatar = AvatarImage(Url("")),
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

        ofy().save().entity(post).now()

        return post
    }

    private fun createComments(postId: Long): Map<Key<Comment>, Comment> {
        return (1..60)
            .map {
                Comment(
                    id = it.toLong(),
                    postId = PostId(postId),
                    author = Author(
                        id = 3,
                        displayName = "",
                        avatar = AvatarImage(Url("")),
                        verified = false
                    ),
                    approved = false,
                    content = CommentContent("")
                )
            }.let { return@let ofy().save().entities(it).now() }
    }
}