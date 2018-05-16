package com.yoloo.server.comment.requestpayload

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.NotBlank

@NoArg
class InsertCommentPayload(
    @field:NotBlank
    val content: String?
)