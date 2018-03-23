package com.yoloo.server.core.user.domain

import com.yoloo.server.core.util.NoArg

@NoArg
data class UserDisplayName(var displayName: String, var slug: String = displayName.toLowerCase().replace(" ", ""))