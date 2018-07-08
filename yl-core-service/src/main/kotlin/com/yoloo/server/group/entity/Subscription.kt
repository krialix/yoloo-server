package com.yoloo.server.group.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url
import com.yoloo.server.group.vo.DisplayName
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
@Entity
data class Subscription(
    @Id
    var id: String,

    @Index
    var userId: Long = extractUserId(id),

    @Index
    var groupId: Long = extractGroupId(id),

    var displayName: DisplayName,

    var profileImageUrl: Url
) : BaseEntity<Subscription>() {

    companion object {
        const val KEY_FILTER_SUBSCRIPTION = "FILTER_SUBSCRIPTION"

        const val INDEX_USER_ID = "userId"
        const val INDEX_GROUP_ID = "groupId"

        fun create(
            requesterId: Long,
            groupId: Long,
            requesterDisplayName: String,
            requesterAvatarImageUrl: String
        ): Subscription {
            return Subscription(
                id = createId(requesterId, groupId),
                displayName = DisplayName(requesterDisplayName),
                profileImageUrl = Url(requesterAvatarImageUrl)
            )
        }

        fun createId(userId: Long, groupId: Long): String {
            return "$userId:$groupId"
        }

        fun createKey(userId: Long, groupId: Long): Key<Subscription> {
            return Key.create(Subscription::class.java, createId(userId, groupId))
        }

        fun isSubscribed(filter: NanoCuckooFilter, userId: Long, groupId: Long): Boolean {
            return filter.contains(Subscription.createId(userId, groupId))
        }

        fun extractUserId(id: String): Long {
            return id.substring(0, id.indexOf(':')).toLong()
        }

        fun extractGroupId(id: String): Long {
            return id.substring(id.lastIndexOf(':')).toLong()
        }
    }
}