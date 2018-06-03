package com.yoloo.server.common.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class AvatarImage(var url: Url) : ValueObject<AvatarImage>