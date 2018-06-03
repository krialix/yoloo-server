package com.yoloo.server.group.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.ValueObject

@NoArg
data class DisplayName(var value: String) : ValueObject<DisplayName>