package com.yoloo.server.auth.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Provider(var id: String?, var type: Type) {

    enum class Type {
        EMAIL,
        GOOGLE,
        FACEBOOK
    }
}