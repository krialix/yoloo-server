package com.yoloo.server.post.fetcher

import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.vo.UserInfoResponse

class StubUserInfoFetcher : Fetcher<Long, UserInfoResponse> {

    override fun fetch(id: Long): UserInfoResponse {
        return UserInfoResponse(
            id = 1,
            self = true,
            displayName = "stub name",
            image = "",
            verified = false
        )
    }
}