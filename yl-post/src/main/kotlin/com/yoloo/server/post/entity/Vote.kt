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
    var userId: Long = extractUserIdFromId(id),

    @Index
    var votableId: Long = extractVotableIdFromId(id)
) {

    companion object {
        const val INDEX_VOTABLE_ID = "votableId"

        const val KEY_FILTER_VOTE = "FILTER_VOTE"

        fun createId(userId: Long, votableId: Long, identifier: String): String {
            return "$userId:$votableId:$identifier"
        }

        fun createKey(userId: Long, votableId: Long, identifier: String): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId, votableId, identifier))
        }

        private fun extractVotableIdFromId(id: String): Long {
            return id.substring(id.indexOf(':') + 1, id.lastIndexOf(':')).toLong()
        }

        private fun extractUserIdFromId(id: String): Long {
            return id.substring(0, id.indexOf(':')).toLong()
        }

        fun isVoted(filter: NanoCuckooFilter, requesterId: Long, postId: Long, identifier: String): Boolean {
            return filter.contains(Vote.createId(requesterId, postId, identifier))
        }
    }
}