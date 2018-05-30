package com.yoloo.server.group.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.group.vo.Description
import com.yoloo.server.group.vo.DisplayName
import com.yoloo.server.group.vo.GroupCountData

@NoArg
@Cache
@Entity
class Group(
    @Id
    var id: Long,

    var displayName: DisplayName,

    var description: Description,

    var enabled: Boolean = true,

    var private: Boolean = false,

    var countData: GroupCountData = GroupCountData()
) : BaseEntity<Long, Group>() {

    override fun getId(): Long {
        return id
    }

    override fun sameIdentityAs(other: Group?): Boolean {
        return equals(other)
    }


}