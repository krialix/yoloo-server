package com.yoloo.server.post.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.shared.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.CommentContent
import com.yoloo.server.post.vo.PostId

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
) : BaseEntity<Comment>(1L) {

    companion object {
        const val INDEX_POST_ID = "postId.value"
        const val INDEX_AUTHOR_ID = "author.id"
    }
}