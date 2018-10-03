package com.yoloo.server.common.vo

import com.googlecode.objectify.annotation.Ignore
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class Author(
    @Index
    var id: Long,

    var displayName: String,

    var profileImageUrl: Url,

    @Ignore
    var self: Boolean = false
) {

    fun isSelf(userId: Long): Boolean {
        return id == userId
    }
}
