package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.entity.BaseEntity
import net.cinnom.nanocuckoo.NanoCuckooFilter
import java.util.regex.Pattern

@NoArg
@Entity
data class Bookmark(
    @Id
    private var id: String,

    @Index
    private var userId: Long = extractUserId(id),

    @Index
    private var bookmarkableId: Long = extractBookmarkId(id)
) : BaseEntity<Bookmark>() {

    companion object {
        private val PATTERN_DELIMITER = Pattern.compile(":")

        const val KEY_FILTER_BOOKMARK = "FILTER_BOOKMARK"

        const val INDEX_USER_ID = "userId"
        const val INDEX_BOOKMARKABLE_ID = "bookmarkableId"

        fun create(userId: Long, bookmarkableId: Long): Bookmark {
            return Bookmark(createId(userId, bookmarkableId))
        }

        fun createId(userId: Long, bookmarkableId: Long): String {
            return "bookmark:$userId:$bookmarkableId"
        }

        fun createKey(userId: Long, bookmarkableId: Long): Key<Bookmark> {
            return Key.create(Bookmark::class.java, createId(userId, bookmarkableId))
        }

        fun isBookmarked(filter: NanoCuckooFilter, requesterId: Long, bookmarkableId: Long): Boolean {
            return filter.contains(createId(requesterId, bookmarkableId))
        }

        fun getPostKey(bookmarkKey: Key<Bookmark>): Key<Post> {
            val name = bookmarkKey.name
            return Key.create(Post::class.java, name.substring(name.indexOf(':') + 1).toLong())
        }

        private fun extractUserId(id: String): Long {
            return id.split(PATTERN_DELIMITER)[1].toLong()
        }

        private fun extractBookmarkId(id: String): Long {
            return id.split(PATTERN_DELIMITER)[2].toLong()
        }
    }
}
