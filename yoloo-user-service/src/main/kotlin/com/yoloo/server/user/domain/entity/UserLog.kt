package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.Entity
import com.yoloo.server.common.util.NoArg

@NoArg
@Entity
data class UserLog(
    var id: String
)