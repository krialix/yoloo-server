package com.yoloo.server.auth.infrastructure.service

import com.yoloo.server.auth.domain.entity.OauthUser
import com.yoloo.server.auth.domain.vo.OauthUserDetails
import com.yoloo.server.common.util.ServiceExceptions.checkNotFound
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Primary
@Service
class AccountDetailsService : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val oauthUser = ofy()
            .load()
            .type(OauthUser::class.java)
            .filter(OauthUser.INDEX_EMAIL, email)
            .first()
            .now()

        checkNotFound(oauthUser != null, "user.error.not-found")
        checkNotFound(!oauthUser!!.disabled, "user.error.not-found")

        return OauthUserDetails(oauthUser)
    }
}