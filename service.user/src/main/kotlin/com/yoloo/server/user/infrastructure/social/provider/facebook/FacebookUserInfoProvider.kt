package com.yoloo.server.user.infrastructure.social.provider.facebook

import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.RequestPayload
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProvider
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.net.URL

@Lazy
@Component
class FacebookUserInfoProvider(private val urlFetchService: URLFetchService) : UserInfoProvider {

    override fun getUserInfo(payload: RequestPayload): UserInfo {
        val httpResponse = urlFetchService.fetch(URL(FACEBOOK_GRAPH_API))

        val objectMapper = ObjectMapper()
        val response = objectMapper.readValue<FacebookResponse>(httpResponse.content, FacebookResponse::class.java)

        return UserInfo(
            providerId = response.id,
            providerType = ProviderType.FACEBOOK,
            email = response.email,
            picture = response.picture.data.url,
            displayName = payload.displayName ?: response.name
        )
    }

    companion object {
        private const val FACEBOOK_GRAPH_API =
            "https://graph.facebook.com/v2.12/me?fields=id,name,picture.height(2000).width(2000),email&" +
                    "access_token=EAAWjiYXX7LQBACZAuV6hTIb4GVfWyT1BPRw3mGbZBEAk9DkMePrvvs3I7Hd19ahFG47mEz6KJdLBf7ZBZALlxxxuZCdKi01UO3eFb635q9en5jbCc1ScCrIgAB8EzZA3x3zIFCGzuPkLT5hC1lr5WSjHWeEIfqBKsUL3zWTJzKwm0GhgcLabBOhPLTzZA6qS6EZD"
    }
}