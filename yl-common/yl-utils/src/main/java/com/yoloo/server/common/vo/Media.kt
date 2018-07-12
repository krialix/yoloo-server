package com.yoloo.server.common.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Media(var type: Type, var url: Url) {

    enum class Type {
        PHOTO,
        VIDEO
    }
}