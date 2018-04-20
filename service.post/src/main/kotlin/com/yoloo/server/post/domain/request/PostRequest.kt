package com.yoloo.server.post.domain.request

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.Min

@NoArg
data class PostRequest(@get:Min(value = 100, message = "post.invalid.content") var content: String)