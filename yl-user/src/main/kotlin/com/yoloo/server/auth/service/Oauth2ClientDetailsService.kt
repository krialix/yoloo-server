package com.yoloo.server.auth.service

import com.yoloo.server.auth.entity.Oauth2Client
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.stereotype.Service

@Service
class Oauth2ClientDetailsService : ClientDetailsService {

    override fun loadClientByClientId(clientId: String): ClientDetails {
        return ofy().load().type(Oauth2Client::class.java).id(clientId).now()
    }
}