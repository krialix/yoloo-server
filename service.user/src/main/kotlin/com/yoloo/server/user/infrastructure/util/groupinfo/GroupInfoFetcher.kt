package com.yoloo.server.user.infrastructure.util.groupinfo

import com.yoloo.server.user.domain.vo.UserGroup
import java.io.IOException

interface GroupInfoFetcher {

    @Throws(IOException::class)
    fun fetch(ids: Collection<String>): List<UserGroup>
}