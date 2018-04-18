package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg
import java.time.LocalDate

@NoArg
data class DateRange(var from: LocalDate, var to: LocalDate)