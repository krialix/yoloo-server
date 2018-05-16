package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class AppInfo(var firstInstallTime: Long, var lastUpdateTime: Long, var googleAdsId: String)