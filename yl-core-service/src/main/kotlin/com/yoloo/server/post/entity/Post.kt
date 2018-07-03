package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfEmpty
import com.googlecode.objectify.condition.IfNotNull
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.comment.vo.ApprovedCommentId
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.*
import java.time.LocalDateTime
import java.util.*

@NoArg
@Cache(expirationSeconds = Post.CACHE_TTL)
@Entity
data class Post(
    @Id
    var id: Long,

    var type: PostType,

    var author: Author,

    var content: PostContent,

    var flags: Set<@JvmSuppressWildcards PostPermFlag> = EnumSet.noneOf(PostPermFlag::class.java),

    var title: PostTitle,

    var group: PostGroup,

    var tags: Set<@JvmSuppressWildcards PostTag>,

    @IgnoreSave(IfNull::class)
    var approvedCommentId: ApprovedCommentId? = null,

    @Index(IfNotNull::class)
    var bounty: PostBounty? = null,

    @IgnoreSave(IfEmpty::class)
    var attachments: List<@JvmSuppressWildcards PostAttachment> = emptyList(),

    var buddyRequest: BuddyRequest? = null,

    var countData: PostCountData = PostCountData()
) : BaseEntity<Post>() {

    fun markAsDeleted() {
        this.auditData.deletedAt = LocalDateTime.now()
    }

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        flags = flags ?: emptySet()
        @Suppress("USELESS_ELVIS")
        attachments = attachments ?: emptyList()
    }

    companion object {
        const val INDEX_GROUP_ID = "group.id"
        const val INDEX_BOUNTY = "bounty"

        const val CACHE_TTL = 7200

        fun createKey(postId: Long): Key<Post> {
            return Key.create(Post::class.java, postId)
        }
    }
}