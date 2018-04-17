package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.Size

@NoArg
data class PostTitle(@field:Size(min = 5, max = 120, message = "post.invalid.title") var value: String)