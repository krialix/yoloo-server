package com.yoloo.server.comment.domain.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.OnSave
import com.yoloo.server.comment.domain.vo.CommentContent
import com.yoloo.server.comment.domain.vo.PostId
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.Validatable
import com.yoloo.server.common.shared.Likeable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.TimestampIdGenerator
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.vo.Author
import java.time.LocalDateTime

@NoArg
@Entity
data class Comment(
    @Id var id: String = TimestampIdGenerator.generateId(),

    @Index var postId: PostId,

    var author: Author,

    var content: CommentContent,

    var likeCount: Int = 0,

    var approved: Boolean = false,

    var createdAt: LocalDateTime = LocalDateTime.now()
) : Likeable, Keyable<Post>, Validatable {

    override fun getLikeabkeId(): String {
        return id
    }

    @OnSave
    override fun validate() {
        super.validate()
    }
}