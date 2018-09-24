package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.NotBlank

@NoArg
class CreateCommentRequest(@field:NotBlank val content: String?)
