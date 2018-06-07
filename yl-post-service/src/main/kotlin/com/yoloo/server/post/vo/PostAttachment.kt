package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class PostAttachment(var path: String, var url: String)