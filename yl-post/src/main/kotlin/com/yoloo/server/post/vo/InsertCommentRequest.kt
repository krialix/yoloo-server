package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@NoArg
class InsertCommentRequest(
    @field:NotNull
    val postId: Long?,

    @field:NotBlank
    val content: String?
)