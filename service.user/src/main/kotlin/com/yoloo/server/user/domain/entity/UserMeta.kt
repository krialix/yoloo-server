package com.yoloo.server.user.domain.entity

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.AppInfo
import com.yoloo.server.user.domain.vo.Brand
import com.yoloo.server.user.domain.vo.Device

@NoArg
data class UserMeta(
    var id: Long,

    var appInfo: AppInfo,

    var device: Device
)