package com.yoloo.server.comment.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.PostId

@NoArg
@Entity
data class Comment(
    @Id
    var id: Long,

    var postId: PostId,

    var author: Author,

    var content: CommentContent,

    var voteCount: Int = 0,

    var approved: Boolean = false
) : BaseEntity<Comment>() {

    companion object {
        const val INDEX_POST_ID = "postId.value"
        const val INDEX_AUTHOR_ID = "author.id"

        fun createKey(commentId: Long): Key<Comment> {
            return Key.create(Comment::class.java, commentId)
        }
    }
}