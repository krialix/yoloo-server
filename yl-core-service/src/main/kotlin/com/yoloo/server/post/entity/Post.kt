package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfNotNull
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.comment.vo.ApprovedCommentId
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Author
import com.yoloo.server.common.vo.Media
import com.yoloo.server.post.vo.*
import com.yoloo.server.vote.entity.Votable
import java.time.LocalDateTime
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
) : BaseEntity<Post>(), Votable {

    override fun vote() {
        countData.voteCount++
    }

    override fun unvote() {
        countData.voteCount--
    }

    override fun isVotingAllowed(): Boolean {
        return !flags.contains(PostPermFlag.DISABLE_VOTING)
    }

    fun markAsDeleted() {
        auditData.deletedAt = LocalDateTime.now()
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
