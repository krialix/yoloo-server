package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.vo.AppInfo
import com.yoloo.server.user.vo.Brand
import com.yoloo.server.user.vo.Device

@Entity
@NoArg
data class UserMeta(
    @Id var id: Long,

    var appInfo: AppInfo,

    var device: Device
)