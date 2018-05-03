package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg

@NoArg
data class EmailValidResponse(val valid: Boolean)