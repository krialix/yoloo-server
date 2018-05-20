package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import org.hibernate.validator.constraints.UniqueElements
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@NoArg
data class UpdatePostRequest(
    @field:NotBlank
    val content: String? = null,

    @field:Size(max = 10)
    @field:UniqueElements
    @field:NotNull
    val tags: List<String>? = null
)