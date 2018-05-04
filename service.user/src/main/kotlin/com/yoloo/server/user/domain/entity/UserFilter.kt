package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.NoArg
import net.cinnom.nanocuckoo.NanoCuckooFilter

@Cache
@NoArg
@Entity
data class UserFilter(
    @Id var id: String = Filters.KEY_FILTER_USERS,

    var filter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build()
)