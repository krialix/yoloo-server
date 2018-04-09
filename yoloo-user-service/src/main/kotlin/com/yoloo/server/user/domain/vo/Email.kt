package com.yoloo.server.user.domain.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.Email

@NoArg
data class Email(@field:Email(message = "user.invalid.email") @Index var value: String)