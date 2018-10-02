package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Ip

@NoArg
data class Device(
    var brand: String,
    var model: String,
    var screen: Screen,
    var os: Os,
    var ip: Ip
)
