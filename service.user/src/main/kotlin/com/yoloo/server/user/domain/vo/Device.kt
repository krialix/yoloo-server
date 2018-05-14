package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Device(
    var brand: Brand,
    var model: String,
    var screen: Screen,
    var os: Os,
    var localIp: IP
)