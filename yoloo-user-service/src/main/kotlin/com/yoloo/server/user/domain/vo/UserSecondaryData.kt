package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.OnlineStatus
import java.time.LocalDateTime
import java.util.*

@NoArg
data class UserSecondaryData(
    private var locale: Locale,

    private var onlineStatus: OnlineStatus = OnlineStatus.ONLINE,

    private var countryIsoCode: String,

    private var website: String? = null,

    private var lastPostTime: LocalDateTime? = null
)