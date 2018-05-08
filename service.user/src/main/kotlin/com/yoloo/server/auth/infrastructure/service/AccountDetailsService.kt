package com.yoloo.server.auth.infrastructure.service

import com.yoloo.server.auth.domain.entity.Account
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Primary
@Service
class AccountDetailsService : UserDetailsService {

    override fun loadUserByUsername(email: String?): UserDetails {
        val account = ofy()
            .load()
            .type(Account::class.java)
            .filter(Account.INDEX_EMAIL, email)
            .first()
            .now()

        Account.checkUserExistsAndEnabled(account)

        return User.builder()
            .username(account.email.value)
            .password(account.password?.value)
            .authorities(account.scopes.map(::SimpleGrantedAuthority))
            .accountExpired(account.expired)
            .accountLocked(account.locked)
            .credentialsExpired(account.credentialsExpired)
            .disabled(account.disabled)
            .build()
    }
}