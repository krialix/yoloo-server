package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.util.NoArg
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
data class UserFilterData(
    var subscribedGroupsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build(),

    var postsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build(),

    var commentsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build(),

    var bookmarkedFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build(),

    var followersFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build(),

    var followingsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32).build()
)