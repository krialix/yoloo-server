package com.yoloo.server.user.domain.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class Username(@Index var value: String)