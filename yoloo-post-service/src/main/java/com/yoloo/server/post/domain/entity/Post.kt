package com.yoloo.server.post.domain.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.PostBounty
import com.yoloo.server.post.domain.vo.PostContent
import com.yoloo.server.post.domain.vo.PostOwner
import com.yoloo.server.post.domain.vo.PostTitle
import javafx.geometry.Pos
import java.time.LocalDateTime
import javax.validation.Valid

@NoArg
@Entity
data class Post(
    @Id
    var id: String,
    var owner: PostOwner,
    var title: PostTitle,
    var content: PostContent,
    var approvedCommentId: String,
    var createdAt: LocalDateTime,
    var deletedAt: LocalDateTime? = null,
    @field:Valid var bounty: PostBounty
) : Keyable<Pos>