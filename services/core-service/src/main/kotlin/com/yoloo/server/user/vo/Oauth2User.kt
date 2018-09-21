package com.yoloo.server.user.vo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class Oauth2User(
    val userId: Long,
    val email: String,
    val profileImageUrl: String,
    val fcmToken: String,
    username: String,
    password: String,
    enabled: Boolean,
    accountNonExpired: Boolean,
    credentialsNonExpired: Boolean,
    accountNonLocked: Boolean,
    authorities: Collection<GrantedAuthority>
) : User(
    username,
    password,
    enabled,
    accountNonExpired,
    credentialsNonExpired,
    accountNonLocked,
    authorities
)
