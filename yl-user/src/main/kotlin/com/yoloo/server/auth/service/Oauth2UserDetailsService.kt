package com.yoloo.server.auth.service

import com.yoloo.server.auth.entity.Account
import com.yoloo.server.auth.vo.Oauth2User
import com.yoloo.server.common.util.ServiceExceptions.checkNotFound
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Primary
@Service
class Oauth2UserDetailsService : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val account = ofy()
            .load()
            .type(Account::class.java)
            .filter(Account.INDEX_EMAIL, email)
            .first()
            .now()

        checkNotFound(account != null, "user.error.not_found")
        checkNotFound(!account!!.disabled, "user.error.not_found")

        return Oauth2User(
            userId = account.id,
            email = account.email.value,
            profileImageUrl = account.image.url.value,
            password = account.password?.value ?: "",
            username = account.displayName.value,
            enabled = !account.disabled,
            accountNonExpired = !account.expired,
            credentialsNonExpired = !account.credentialsExpired,
            accountNonLocked = !account.locked,
            authorities = account.authorities.map { SimpleGrantedAuthority(it.name) },
            fcmToken = account.fcmToken,
            updatedAt = account.updatedAt
        )
    }
}