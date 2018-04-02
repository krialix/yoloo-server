package com.yoloo.server.post.domain.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.*
import javafx.geometry.Pos
import java.time.LocalDateTime
import javax.validation.Valid

@NoArg
@Cache
@Entity
data class Post(
    @Id
    var id: String,

    var owner: PostOwner,

    @field:Valid
    var title: PostTitle,

    var approvedCommentId: AcceptedCommentId,

    @field:Valid
    var bounty: PostBounty,

    @field:Valid
    var topic: PostTopic,

    var tags: PostHashTags,

    var type: PostType,

    var permissions: Set<PostPermission> = PostPermission.defaultPermissions(),

    var content: PostContent,

    var attachments: List<PostAttachment> = emptyList(),

    var createdAt: LocalDateTime,

    var deletedAt: LocalDateTime? = null
) : Keyable<Pos>