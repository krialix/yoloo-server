package com.yoloo.server.core.user.domain.model

import com.yoloo.server.core.util.NoArg
import javax.validation.constraints.Email

@NoArg
data class Email(@get:Email(message = "users-3") var email: String)