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
data class Group(
    @Id
    var id: Long,

    var displayName: DisplayName,

    var description: Description,

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
        const val KEY_FILTER_SUBSCRIPTION = "FILTER_SUBSCRIPTION"
    }
}