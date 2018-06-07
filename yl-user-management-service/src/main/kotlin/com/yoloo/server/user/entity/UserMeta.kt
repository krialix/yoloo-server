package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.vo.AppInfo
import com.yoloo.server.user.vo.Device

@Entity
@NoArg
class UserMeta(
    @Id
    var id: Long,

    var appInfo: AppInfo,

    var device: Device
) : BaseEntity<Long, UserMeta>() {

    override fun getId(): Long {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserMeta

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}