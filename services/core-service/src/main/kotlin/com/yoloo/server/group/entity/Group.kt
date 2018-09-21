package com.yoloo.server.group.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url
import com.yoloo.server.group.vo.*
import java.util.*

@NoArg
@Cache
@Entity
data class Group(
    @Id
    var id: Long,

    var displayName: DisplayName,

    var imageUrl: Url,

    var description: Description,

    var topicName: String,

    var countData: GroupCountData = GroupCountData(),

    var owner: Owner? = null,

    var flags: Set<@JvmSuppressWildcards GroupFlag> = EnumSet.noneOf(GroupFlag::class.java)
) : BaseEntity<Group>() {

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        flags = flags ?: emptySet()
    }

    companion object {
        fun createKey(groupId: Long): Key<Group> {
            return Key.create(Group::class.java, groupId)
        }
    }
}
