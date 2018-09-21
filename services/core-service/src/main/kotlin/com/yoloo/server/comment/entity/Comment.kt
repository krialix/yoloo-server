package com.yoloo.server.comment.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.comment.vo.CommentContent
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Author
import com.yoloo.server.entity.Approvable
import com.yoloo.server.entity.Keyable
import com.yoloo.server.entity.Votable
import com.yoloo.server.post.vo.PostId
import java.time.Instant

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

    var approved: Boolean = false,

    var createdAt: Instant = Instant.now()
) : Keyable<Comment>, Votable, Approvable {
    override fun vote() {
        voteCount++
    }

    override fun unvote() {
        voteCount--
    }

    override fun isVotingAllowed(): Boolean {
        return true
    }

    override fun approve() {
        approved = true
    }

    override fun disapprove() {
        approved = false
    }

    override fun isApprovingAllowed(): Boolean {
        return true
    }

    companion object {
        const val INDEX_POST_ID = "postId.value"
        const val INDEX_AUTHOR_ID = "author.id"

        fun createKey(commentId: Long): Key<Comment> {
            return Key.create(Comment::class.java, commentId)
        }
    }
}
