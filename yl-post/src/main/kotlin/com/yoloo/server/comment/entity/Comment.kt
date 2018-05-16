package com.yoloo.server.comment.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.OnSave
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.PostId
import com.yoloo.server.common.shared.Keyable
import com.yoloo.server.common.shared.Likeable
import com.yoloo.server.common.shared.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.Author
import java.time.LocalDateTime

@NoArg
@Entity
data class Comment(
    @Id var id: Long,

    @Index var postId: PostId,

    var author: Author,

    var content: CommentContent,

    var likeCount: Int = 0,

    var approved: Boolean = false,

    var createdAt: LocalDateTime = LocalDateTime.now()
) : Likeable, Keyable<Post>, Validatable {

    override fun getLikeabkeId(): Long {
        return id
    }

    @OnSave
    override fun validate() {
        super.validate()
    }
}