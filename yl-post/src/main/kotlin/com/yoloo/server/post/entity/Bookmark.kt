package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
@Entity
data class Bookmark(
    @Id
    var id: String,

    @Index
    var userId: Long,

    @Index
    var postId: Long
) {

    companion object {
        fun createId(userId: Long, postId: Long): String {
            return "$userId:$postId"
        }

        fun createKey(userId: Long, postId: Long): Key<Bookmark> {
            return Key.create(Bookmark::class.java, createId(userId, postId))
        }
    }
}