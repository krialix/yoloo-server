package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class LocaleResponse(val language: String, val country: String)