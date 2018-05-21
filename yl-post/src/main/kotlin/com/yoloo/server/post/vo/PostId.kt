package com.yoloo.server.post.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class PostId(@Index var value: Long)