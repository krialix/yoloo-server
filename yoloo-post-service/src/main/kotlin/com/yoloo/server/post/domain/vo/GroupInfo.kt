package com.yoloo.server.post.domain.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class GroupInfo(@Index var id: String, var displayName: String)