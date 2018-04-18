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
import javax.validation.constraints.Size

@NoArg
@Cache
@Entity
data class Post(
    @Id
    var id: String = TimestampIdGenerator.generateId(),

    var type: PostType,

    var author: Author,

    var groupInfo: GroupInfo,

    @field:Valid
    var title: PostTitle,

    @IgnoreSave(IfNull::class)
    var approvedCommentId: AcceptedCommentId? = null,

    @field:Valid
    var bounty: PostBounty? = null,

    @field:Valid
    var topic: PostTopic,

    @field:Size(max = 10)
    @Index
    var tags: Set<@JvmSuppressWildcards PostTag>,

    var permissions: Set<@JvmSuppressWildcards PostPermission> = PostPermission.defaultPermissions(),

    var content: PostContent,

    var attachments: List<@JvmSuppressWildcards PostAttachment>? = emptyList(),

    var buddyRequestInfo: BuddyRequestInfo? = null,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Index
    @IgnoreSave(IfNull::class)
    var deletedAt: LocalDateTime? = null
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