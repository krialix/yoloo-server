package com.yoloo.server.post.domain.entity

import com.fasterxml.uuid.Generators
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.*
import java.time.LocalDateTime
import javax.validation.Valid

@NoArg
@Cache
@Entity
data class Post(
    @Id
    var id: String = Generators.timeBasedGenerator().generate().toString().replace("-", ""),

    var owner: PostOwner,

    @field:Valid
    var title: PostTitle,

    var approvedCommentId: AcceptedCommentId? = null,

    @field:Valid
    var bounty: PostBounty = PostBounty(),

    @field:Valid
    var topic: PostTopic,

    @Index var tags: Set<@JvmSuppressWildcards PostTag>,

    var type: PostType,

    var permissions: Set<@JvmSuppressWildcards PostPermission> = PostPermission.defaultPermissions(),

    var content: PostContent,

    var attachments: List<@JvmSuppressWildcards PostAttachment> = emptyList(),

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var deletedAt: LocalDateTime? = null
) : Keyable<Post>