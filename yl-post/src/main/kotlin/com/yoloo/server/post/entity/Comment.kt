package com.yoloo.server.post.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.CommentContent
import com.yoloo.server.post.vo.PostId

@NoArg
@Entity
class Comment(
    @Id
    var id: Long,

    @Index
    var postId: PostId,

    var author: Author,

    var content: CommentContent,

    var voteCount: Int = 0,

    var approved: Boolean = false
) : BaseEntity<Long, Comment>() {

    override fun getId(): Long {
        return id
    }

    override fun sameIdentityAs(other: Comment): Boolean {
        return equals(other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (id != other.id) return false
        if (postId != other.postId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + postId.hashCode()
        return result
    }

    companion object {
        const val INDEX_POST_ID = "postId.value"
        const val INDEX_AUTHOR_ID = "author.id"
    }
}