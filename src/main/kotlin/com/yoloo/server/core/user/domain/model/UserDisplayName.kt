package com.yoloo.server.core.user.domain.model

import com.yoloo.server.core.util.NoArg
import javax.validation.Valid

@NoArg
data class UserDisplayName @Valid constructor(
    var displayName: String,

    var slug: String = displayName.toLowerCase().replace(" ", "")
)