package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import java.time.LocalDate

@NoArg
data class BuddyRequest(
    var peopleRange: Range<Int>,
    var location: Location,
    var dateRange: Range<LocalDate>
)