package com.yoloo.server.post.domain.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class PostAttachment(var path: String, var url: String)