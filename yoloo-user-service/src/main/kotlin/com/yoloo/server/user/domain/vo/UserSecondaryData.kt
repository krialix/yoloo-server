package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import java.time.LocalDateTime
import java.util.*

@NoArg
data class UserSecondaryData(
    var locale: Locale,

    var onlineStatus: OnlineStatus = OnlineStatus.ONLINE,

    var countryIsoCode: String,

    var bio: String? = null,

    var website: String? = null,

    var lastPostTime: LocalDateTime? = null
)