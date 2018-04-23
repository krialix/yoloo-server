package com.yoloo.server.user.infrastructure.social.configuration

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.cloud.http.HttpTransportOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SocialProviderConfiguration {

    @Bean
    fun httpTransport(): HttpTransport {
        return HttpTransportOptions.DefaultHttpTransportFactory().create()
    }

    @Bean
    fun jacksonFactory(): JacksonFactory {
        return JacksonFactory.getDefaultInstance()
    }

    @Bean
    fun googleIdTokenVerifier(httpTransport: HttpTransport, jacksonFactory: JacksonFactory): GoogleIdTokenVerifier {
        return GoogleIdTokenVerifier.Builder(httpTransport, jacksonFactory)
            .setAudience(listOf("CLIENT_ID_1"))
            .build()
    }
}