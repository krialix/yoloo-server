package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@NoArg
data class BuddyInfo(
    @field:NotNull
    val fromPeople: Int?,

    @field:NotNull
    val toPeople: Int?,

    @field:NotBlank
    val location: String?,

    @field:NotNull
    val fromDate: LocalDate?,

    @field:NotNull
    val toDate: LocalDate?
)