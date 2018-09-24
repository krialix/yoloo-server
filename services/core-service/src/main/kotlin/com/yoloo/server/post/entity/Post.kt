package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfNotNull
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.post.vo.ApprovedCommentId
import com.yoloo.server.post.vo.Commentable
import com.yoloo.server.common.SoftDelete
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Author
import com.yoloo.server.common.vo.Media
import com.yoloo.server.entity.BaseEntity
import com.yoloo.server.entity.Likeable
import com.yoloo.server.post.vo.*
import java.time.Instant
import java.util.*

@NoArg
@Cache(expirationSeconds = Post.CACHE_TTL)
@Entity
data class Post(
    @Id
    var id: Long,

    var author: Author,

    var title: PostTitle,

    var content: PostContent,

    var group: PostGroup,

    var tags: Set<String>,

    var medias: List<@JvmSuppressWildcards Media>,

    var flags: Set<@JvmSuppressWildcards PostPermFlag> = EnumSet.noneOf(PostPermFlag::class.java),

    @IgnoreSave(IfNull::class)
    var approvedCommentId: ApprovedCommentId? = null,

    @Index(IfNotNull::class)
    var bounty: PostBounty? = null,

    var buddyRequest: BuddyRequest? = null,

    var countData: PostCountData = PostCountData()
) : BaseEntity<Post>(), Likeable, Commentable, SoftDelete {
    override fun isSoftDeleted(): Boolean {
        return auditData.isDeleted
    }

    override fun isVotingAllowed(): Boolean {
        return !flags.contains(PostPermFlag.DISABLE_VOTING)
    }

    override fun incCommentCount() {
        countData.commentCount++
    }

    override fun decCommentCount() {
        countData.commentCount--
    }

    override fun isCommentingAllowed(): Boolean {
        return !flags.contains(PostPermFlag.DISABLE_COMMENTING)
    }

    fun markAsDeleted() {
        auditData.deletedAt = Instant.now()
    }

    fun approve(commentId: Long) {
        approvedCommentId = ApprovedCommentId(commentId)
    }

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        flags = flags ?: emptySet()
    }

    companion object {
        const val INDEX_GROUP_ID = "group.id"
        const val INDEX_BOUNTY = "bounty"

        const val CACHE_TTL = 7200 //ms

        @JvmStatic
        fun createKey(postId: Long): Key<Post> {
            return Key.create(Post::class.java, postId)
        }
    }
}
