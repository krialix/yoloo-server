package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg
import java.time.LocalDate

@NoArg
data class BuddyRequestInfo(var peopleRange: Range<Int>, var location: Location, var dateRange: Range<LocalDate>)