package com.yoloo.server.post.domain.entity

import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.TimestampIdGenerator
import com.yoloo.server.post.domain.vo.*
import java.time.LocalDateTime
import javax.validation.Valid

@NoArg
@Cache
@Entity
data class Post(
    @Id
    var id: String = TimestampIdGenerator.generateId(),

    var author: Author,

    @field:Valid
    var title: PostTitle,

    @IgnoreSave(IfNull::class)
    var approvedCommentId: AcceptedCommentId? = null,

    @field:Valid
    var bounty: PostBounty? = null,

    @field:Valid
    var topic: PostTopic,

    @Index
    var tags: Set<@JvmSuppressWildcards PostTag>,

    var type: PostType,

    var permissions: Set<@JvmSuppressWildcards PostPermission> = PostPermission.defaultPermissions(),

    var content: PostContent,

    var attachments: List<@JvmSuppressWildcards PostAttachment>? = emptyList(),

    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Index
    @IgnoreSave(IfNull::class)
    var deletedAt: LocalDateTime? = null,

    @Ignore
    var voteDir: Int = 0,

    @Ignore
    var voteCount: Int = 0,

    @Ignore
    var commentCount: Int = 0
) : Keyable<Post>, Validatable {

    @OnSave
    override fun validate() {
        super.validate()
        attachments = attachments ?: emptyList()
        bounty = bounty ?: PostBounty()
    }

    fun isDeleted(): Boolean {
        return deletedAt != null
    }
}