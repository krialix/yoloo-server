package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@NoArg
data class CreatePostRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 120, message = "post.invalid.title")
    var title: String?,

    @field:NotBlank
    val content: String?,

    @field:NotNull
    val groupId: Long?,

    @field:Size(max = 10)
    @field:UniqueElements
    @field:NotNull
    val tags: List<String>?,

    @field:PositiveOrZero
    val coin: Int = 0,

    @field:UniqueElements
    val medias: List<MultipartFile>?,

    @field:Valid
    val buddyInfo: BuddyInfo?
)