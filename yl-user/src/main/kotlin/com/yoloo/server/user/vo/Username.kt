package com.yoloo.server.user.vo

import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg

@NoArg
data class Username(@Index var value: String)