package com.yoloo.server.comment.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.comment.vo.PostId
import com.yoloo.server.common.shared.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.Author

@NoArg
@Entity
data class Comment(
    @Id
    var id: Long,

    @Index
    var postId: PostId,

    var author: Author,

    var content: CommentContent,

    var voteCount: Int = 0,

    var approved: Boolean = false
) : BaseEntity<Comment>(1L)