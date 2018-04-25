package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.infrastructure.social.ProviderType

@NoArg
data class SocialProvider(var id: String?, var type: ProviderType)