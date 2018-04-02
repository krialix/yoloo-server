package com.yoloo.server.post.domain.vo

import javax.validation.constraints.PositiveOrZero

data class PostBounty(@field:PositiveOrZero var value: Int)