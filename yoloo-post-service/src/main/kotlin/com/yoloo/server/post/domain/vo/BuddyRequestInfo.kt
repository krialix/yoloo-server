package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class BuddyRequestInfo(var peopleRange: Range, var location: Location, var dateRange: DateRange)