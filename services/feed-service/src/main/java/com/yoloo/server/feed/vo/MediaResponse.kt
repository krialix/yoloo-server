package com.yoloo.server.feed.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class MediaResponse(val type: String, val mediaUrl: String)