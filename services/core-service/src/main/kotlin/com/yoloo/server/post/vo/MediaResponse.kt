package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class MediaResponse(val type: String, val mediaUrl: String)