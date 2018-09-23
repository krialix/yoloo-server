package com.yoloo.server.like.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.entity.Keyable
import net.cinnom.nanocuckoo.NanoCuckooFilter
import java.time.Instant
import java.util.regex.Pattern

@Entity
@NoArg
data class Like(
        @Id
        var id: String,

        @Index
        var userId: Long = extractUserId(id),

        @Index
        var likeableId: Long = extractLikeableId(id),

        @Index
        var createdAt: Instant = Instant.now()
) : Keyable<Like> {

    companion object {
        private val PATTERN_DELIMITER = Pattern.compile(":")

        const val INDEX_LIKEABLE_ID = "likeableId"
        const val INDEX_USER_ID = "userId"
        const val INDEX_CREATED_AT = "createdAt"

        const val KEY_FILTER_VOTE = "FILTER_VOTE"

        fun create(userId: Long, likeableId: Long): Like {
            return Like(createId(userId, likeableId))
        }

        fun createId(userId: Long, likeableId: Long): String {
            return "like:$userId:$likeableId"
        }

        fun createKey(userId: Long, likeableId: Long): Key<Like> {
            return Key.create(Like::class.java, createId(userId, likeableId))
        }

        private fun extractUserId(id: String): Long {
            return id.split(PATTERN_DELIMITER)[1].toLong()
        }

        private fun extractLikeableId(id: String): Long {
            return id.split(PATTERN_DELIMITER)[2].toLong()
        }

        fun isVoted(
                filter: NanoCuckooFilter,
                requesterId: Long,
                postId: Long
        ): Boolean {
            return filter.contains(createId(requesterId, postId))
        }
    }
}
