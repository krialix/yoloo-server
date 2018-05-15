package com.yoloo.server.auth.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserLocale(var language: String, var country: String)