package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url

@NoArg
data class Media(var type: Media.Type, var url: Url) {

    enum class Type {
        PHOTO,
        VIDEO
    }
}