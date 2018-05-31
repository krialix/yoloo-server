package com.yoloo.server.group.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.group.vo.*
import java.util.*

@NoArg
@Cache
@Entity
class Group(
    @Id
    var id: Long,

    var displayName: DisplayName,

    var description: Description,

    var countData: GroupCountData = GroupCountData(),

    var owner: Owner,

    var flags: Set<@JvmSuppressWildcards GroupFlag> = EnumSet.noneOf(GroupFlag::class.java)
) : BaseEntity<Long, Group>() {

    override fun getId(): Long {
        return id
    }

    override fun sameIdentityAs(other: Group?): Boolean {
        return equals(other)
    }

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        flags = flags ?: emptySet()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val KEY_FILTER_SUBSCRIPTION = "FILTER_SUBSCRIPTION"
    }
}