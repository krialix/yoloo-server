package com.yoloo.server.user.service

import com.yoloo.server.user.vo.Oauth2User
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Primary
@Service
class Oauth2UserDetailsService(private val passwordEncoder: PasswordEncoder) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        /*val user = ofy()
            .load()
            .type(User::class.java)
            .filter(User.INDEX_EMAIL, email)
            .first()
            .now()

        checkNotFound(user != null, "user.error.not_found")
        checkNotFound(!user!!.disabled, "user.error.not_found")

        return Oauth2User(
            userId = user.id,
            email = user.email.value,
            profileImageUrl = user.profile.image.url.value,
            password = user.password?.value ?: "",
            username = user.profile.displayName.value,
            enabled = !user.disabled,
            accountNonExpired = !user.expired,
            credentialsNonExpired = !user.credentialsExpired,
            accountNonLocked = !user.locked,
            authorities = user.authorities.map { SimpleGrantedAuthority(it.name) },
            fcmToken = user.fcmToken
        )*/

        return Oauth2User(
            userId = 1,
            email = "demo@demo.com",
            profileImageUrl = "http://image.com",
            password = passwordEncoder.encode(email),
            username = "demo",
            enabled = true,
            accountNonExpired = true,
            credentialsNonExpired = true,
            accountNonLocked = true,
            authorities = emptyList(),
            fcmToken = ""
        )
    }
}