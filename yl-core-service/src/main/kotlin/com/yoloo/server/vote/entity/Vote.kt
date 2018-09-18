package com.yoloo.server.vote.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Keyable
import net.cinnom.nanocuckoo.NanoCuckooFilter

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
        const val INDEX_VOTABLE_ID = "votableId"

        const val KEY_FILTER_VOTE = "FILTER_VOTE"

        fun create(userId: Long, votableId: Long): Vote {
            return Vote(createId(userId, votableId))
        }

        fun createId(userId: Long, votableId: Long): String {
            return "$userId:$votableId"
        }

        fun createKey(userId: Long, votableId: Long): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId, votableId))
        }

        private fun extractUserId(id: String): Long {
            return id.substring(0, id.indexOf(':')).toLong()
        }

        private fun extractVotableId(id: String): Long {
            return id.substring(id.indexOf(':') + 1, id.lastIndexOf(':')).toLong()
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
