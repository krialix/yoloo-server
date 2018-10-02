package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url

@NoArg
data class Profile(
    var displayName: DisplayName,

    var profileImageUrl: Url,

    var profileUrl: Url? = null,

    var gender: Gender,

    var spokenLanguages: List<@JvmSuppressWildcards Language> = emptyList(),

    var websiteUrl: Url? = null,

    var about: About? = null,

    var locale: UserLocale
)
