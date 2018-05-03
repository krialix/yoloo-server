package com.yoloo.server.comment.domain.requestpayload

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.NotBlank

@NoArg
class InsertCommentPayload(
    @field:NotBlank
    val content: String?
)