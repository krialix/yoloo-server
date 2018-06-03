package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
class Os(var type: Type, var version: Version) {

    enum class Type {
        ANDROID,
        IOS,
        WINDOWS
    }

    @NoArg
    data class Version(var value: String)
}