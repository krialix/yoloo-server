package com.yoloo.server.user.infrastructure.social.provider.facebook

import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProvider
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.net.URL

@Lazy
@Component
class FacebookUserInfoProvider(private val urlFetchService: URLFetchService) : UserInfoProvider {

    override fun getUserInfo(token: String): UserInfo {
        val httpResponse = urlFetchService.fetch(URL("$FACEBOOK_GRAPH_API$token"))

        val objectMapper = ObjectMapper()
        val response = objectMapper.readValue<FacebookResponse>(httpResponse.content, FacebookResponse::class.java)

        return UserInfo(
            providerId = response.id,
            providerType = ProviderType.FACEBOOK,
            picture = response.picture.data.url
        )
    }

    companion object {
        private const val FACEBOOK_GRAPH_API =
            "https://graph.facebook.com/v2.12/me?fields=id,picture.height(2000).width(2000)&access_token="
    }
}