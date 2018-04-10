package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.Max

@NoArg
data class PostTitle(@field:Max(value = 120, message = "post.invalid.title") var value: String)