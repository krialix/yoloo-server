package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.Valid

@NoArg
data class UserDisplayName @Valid constructor(
    private var displayName: String,

    private var slug: String = displayName.toLowerCase().replace(" ", "")
)