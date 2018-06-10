package com.yoloo.server.post.usecase

import com.yoloo.server.common.util.AppEngineRule
import com.yoloo.server.common.util.TestObjectifyService.ofy
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import org.junit.Rule

class ApproveCommentUseCaseIntegrationTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    /*private val approveCommentUseCase by lazy(LazyThreadSafetyMode.NONE) { ApproveCommentUseCase() }

    @Before
    fun setUp() {
        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().register(Comment::class.java)
        fact().register(Post::class.java)
    }

    @Test
    fun approveComment_whenNoApprovedBefore_shouldApprove() {
        val post = createPost()
        val comment = createComment()

        approveCommentUseCase.execute(2, 1)

        val fetchedPost = ofy().saveClearLoad(post)
        val fetchedComment = ofy().saveClearLoad(comment)

        assertThat(fetchedComment.approved).isTrue()
        assertThat(fetchedPost.approvedCommentId).isNotNull()
        assertThat(fetchedPost.approvedCommentId!!.value).isEqualTo(fetchedComment.id)
    }

    @Test(expected = ForbiddenException::class)
    fun approveComment_whenNoApprovedBeforeAndDifferentUser_shouldThrowForbidden() {
        createPost()
        createComment()

        approveCommentUseCase.execute(1, 1)
    }

    @Test(expected = BadRequestException::class)
    fun approveComment_whenApprovedBefore_shouldThrowBadRequest() {
        createPost()
        createComment(true)

        approveCommentUseCase.execute(1, 1)
    }*/

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

    private fun createComment(approved: Boolean = false): Comment {
        val comment = Comment(
            id = 1,
            postId = PostId(1, 2),
            author = Author(
                id = 3,
                displayName = "",
                avatar = AvatarImage(Url("")),
                verified = false
            ),
            approved = approved,
            content = CommentContent("")
        )

        ofy().save().entity(comment).now()

        return comment
    }
}