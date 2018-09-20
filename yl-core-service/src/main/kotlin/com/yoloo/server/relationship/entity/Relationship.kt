package com.yoloo.server.relationship.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url
import com.yoloo.server.relationship.vo.Relationable
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.DisplayName
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
@Entity
data class Relationship(
        @Id
        var id: String,

        @Index
        var fromId: Long = extractFromId(id),

        @Index
        var toId: Long = extractToId(id),

        var fromDisplayName: DisplayName,

        var fromProfileImageUrl: Url
) : BaseEntity<Relationship>() {

    companion object {
        const val KEY_FILTER_RELATIONSHIP = "FILTER_RELATIONSHIP"

        const val INDEX_FROM_ID = "fromId"
        const val INDEX_TO_ID = "toId"

        fun create(from: Relationable, to: Relationable): Relationship {
            return Relationship(
                    id = Relationship.createId(from.getRelationableId(), to.getRelationableId()),
                    fromDisplayName = from.getRelationableDisplayName(),
                    fromProfileImageUrl = from.getRelationableProfileImageUrl()
            )
        }

        fun createId(fromId: Long, toId: Long): String {
            return "$fromId:$toId"
        }

        fun createKey(fromId: Long, toId: Long): Key<Relationship> {
            return Key.create(Relationship::class.java, createId(fromId, toId))
        }

        fun isFollowing(filter: NanoCuckooFilter, fromId: Long, toId: Long): Boolean {
            return filter.contains(createId(fromId, toId))
        }

        fun extractFromId(id: String): Long {
            return id.substring(0, id.indexOf(':')).toLong()
        }

        fun extractToId(id: String): Long {
            return id.substring(id.lastIndexOf(':')).toLong()
        }

        fun getFromKey(relationshipKey: Key<Relationship>): Key<User> {
            val name = relationshipKey.name
            return Key.create(User::class.java, name.substring(name.indexOf(':') + 1).toLong())
        }
    }

    enum class Type {
        FOLLOWING,
        FOLLOWER
    }
}