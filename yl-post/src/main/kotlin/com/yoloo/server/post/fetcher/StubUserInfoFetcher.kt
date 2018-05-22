package com.yoloo.server.post.fetcher

import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.vo.UserInfoResponse
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Lazy
@Profile("dev")
@Component
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