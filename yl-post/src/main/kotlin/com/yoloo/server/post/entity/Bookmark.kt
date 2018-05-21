package com.yoloo.server.post.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg

@Cache(expirationSeconds = Bookmark.CACHE_EXPIRATION_TIME)
@NoArg
@Entity
data class Bookmark(@Id var id: String, var ids: List<Long> = emptyList()) {

    companion object {
        const val CACHE_EXPIRATION_TIME = 1800

        fun createId(userId: Long): String {
            return "$userId:1"
        }

        fun createKey(userId: Long): Key<Vote> {
            return Key.create(Vote::class.java, createId(userId))
        }
    }
}