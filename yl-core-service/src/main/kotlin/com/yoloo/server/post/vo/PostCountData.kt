package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class PostCountData(var commentCount: Int = 0, var voteCount: Int = 0)