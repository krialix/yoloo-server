package com.yoloo.server.post.request

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@NoArg
data class PostRequest(

    @field:NotNull(message = "post.title.null")
    var title: String?,

    @field:NotBlank(message = "post.content.null")
    @field:Min(value = 100, message = "post.content.invalid")
    var content: String?,

    @field:NotBlank(message = "post.groupId.null")
    var groupId: Long?,

    @field:NotEmpty(message = "post.tags.empty")
    var tags: List<String>?,

    var attachmentIds: List<String>?
)