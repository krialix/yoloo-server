package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.RegexUtil
import javax.validation.constraints.Pattern

@NoArg
data class IP(@field:Pattern(regexp = RegexUtil.IP_REGEXP, message = "users-4") var value: String)