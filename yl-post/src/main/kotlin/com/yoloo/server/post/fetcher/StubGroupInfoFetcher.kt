package com.yoloo.server.post.fetcher

import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.vo.GroupInfoResponse
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Lazy
@Profile("dev")
@Component
class StubGroupInfoFetcher : Fetcher<Long, GroupInfoResponse> {

    override fun fetch(id: Long): GroupInfoResponse {
        return GroupInfoResponse(1, "Group 1")
    }
}