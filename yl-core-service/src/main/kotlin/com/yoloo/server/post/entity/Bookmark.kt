package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
@Entity
data class Bookmark(
    @Id
    private var id: String,

    @Index
    private var userId: Long = extractUserId(id),

    @Index
    private var postId: Long = extractPostId(id)
) : BaseEntity<Bookmark>() {

    companion object {
        const val KEY_FILTER_BOOKMARK = "FILTER_BOOKMARK"

        const val INDEX_USER_ID = "userId"
        const val INDEX_POST_ID = "postId"

        fun create(userId: Long, postId: Long) : Bookmark {
            return Bookmark(createId(userId, postId))
        }

        fun createId(userId: Long, postId: Long): String {
            return "$userId:$postId"
        }

        fun createKey(userId: Long, postId: Long): Key<Bookmark> {
            return Key.create(Bookmark::class.java, createId(userId, postId))
        }

        fun isBookmarked(filter: NanoCuckooFilter, requesterId: Long, postId: Long): Boolean {
            return filter.contains(Bookmark.createId(requesterId, postId))
        }

        fun getPostKey(bookmarkKey: Key<Bookmark>): Key<Post> {
            val name = bookmarkKey.name
            return Key.create(Post::class.java, name.substring(name.indexOf(':') + 1).toLong())
        }

        private fun extractUserId(id: String): Long {
            return id.substring(0, id.indexOf(':')).toLong()
        }

        private fun extractPostId(id: String): Long {
            return id.substring(id.lastIndexOf(':')).toLong()
        }
    }
}