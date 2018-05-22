package com.yoloo.server.post.fetcher

import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.vo.UserInfoResponse
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Lazy
@Profile("!dev")
@Component
class DefaultUserInfoFetcher : Fetcher<Long, UserInfoResponse> {

    override fun fetch(id: Long): UserInfoResponse {
        val response = RestTemplate().getForEntity(USER_INFO_ENDPOINT, UserInfoResponse::class.java)
        return response.body!!
    }

    companion object {
        private const val USER_INFO_ENDPOINT = "http://localhost:8080/api/v1/auth/userinfo"
    }
}