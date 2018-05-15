package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.AppInfo
import com.yoloo.server.user.domain.vo.Brand
import com.yoloo.server.user.domain.vo.Device

@Entity
@NoArg
data class UserMeta(
    @Id var id: Long,

    var appInfo: AppInfo,

    var device: Device
)