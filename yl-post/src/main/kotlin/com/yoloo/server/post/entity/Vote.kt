package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@Entity
@NoArg
data class Vote(
    @Id
    var id: String,

    @Index
    var votableId: Long = extractVotableIdFromId(id)
) {

    companion object {
        const val INDEX_VOTABLE_ID = "votableId"

        fun createId(userId: Long, votableId: Long, identifier: String): String {
            return "$userId:$votableId:$identifier"
        }

        fun createKey(userId: Long, votableId: Long, identifier: String): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId, votableId, identifier))
        }

        private fun extractVotableIdFromId(id: String): Long {
            return id.substring(id.indexOf(':') + 1, id.lastIndexOf(':')).toLong()
        }
    }
}