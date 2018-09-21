package com.yoloo.server.vote.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.entity.Keyable
import net.cinnom.nanocuckoo.NanoCuckooFilter
import java.util.regex.Pattern

@Entity
@NoArg
data class Vote(
    @Id
    var id: String,

    @Index
    var userId: Long = extractUserId(id),

    @Index
    var votableId: Long = extractVotableId(id)
) : Keyable<Vote> {

    companion object {
        private val PATTERN_DELIMITER = Pattern.compile(":")

        const val INDEX_VOTABLE_ID = "votableId"

        const val KEY_FILTER_VOTE = "FILTER_VOTE"

        fun create(userId: Long, votableId: Long): Vote {
            return Vote(createId(userId, votableId))
        }

        fun createId(userId: Long, votableId: Long): String {
            return "vote:$userId:$votableId"
        }

        fun createKey(userId: Long, votableId: Long): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId, votableId))
        }

        private fun extractUserId(id: String): Long {
            return id.split(PATTERN_DELIMITER)[1].toLong()
        }

        private fun extractVotableId(id: String): Long {
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
