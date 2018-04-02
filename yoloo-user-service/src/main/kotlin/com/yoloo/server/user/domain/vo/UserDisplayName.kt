package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.Valid

@NoArg
data class UserDisplayName @Valid constructor(
    var value: String,

    var slug: String = value.toLowerCase().replace(" ", "")
)