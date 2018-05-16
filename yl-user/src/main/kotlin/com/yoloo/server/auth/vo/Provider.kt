package com.yoloo.server.auth.vo

import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.condition.IfNotNull
import com.yoloo.server.common.util.NoArg

@NoArg
data class Provider(@Index(IfNotNull::class) var id: String?, var type: Type) {

    enum class Type {
        EMAIL,
        GOOGLE,
        FACEBOOK
    }
}