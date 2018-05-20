package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg

@Entity
@NoArg
data class Vote(
    @Id
    var id: String,

    var direction: Int
) {

    companion object {
        fun createId(userId: Long, votableId: Long, identifier: String) : String {
            return "$userId:$votableId:$identifier"
        }

        fun createKey(userId: Long, votableId: Long, identifier: String): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId, votableId, identifier))
        }
    }
}