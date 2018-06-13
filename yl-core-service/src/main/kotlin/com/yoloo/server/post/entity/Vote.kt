package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
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
) {

    companion object {
        const val INDEX_VOTABLE_ID = "votableId"

        const val KEY_FILTER_VOTE = "FILTER_VOTE"

        fun create(userId: Long, votableId: Long, identifier: String): Vote {
            return Vote(createId(userId, votableId, identifier))
        }

        fun createId(userId: Long, votableId: Long, identifier: String): String {
            return "$userId:$votableId:$identifier"
        }

        fun createKey(userId: Long, votableId: Long, identifier: String): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId, votableId, identifier))
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
            postId: Long,
            identifier: String
        ): Boolean {
            return filter.contains(Vote.createId(requesterId, postId, identifier))
        }
    }
}