package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Range<T>(var from: T, var to: T)