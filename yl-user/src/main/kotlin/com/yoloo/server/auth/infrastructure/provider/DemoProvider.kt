package com.yoloo.server.auth.infrastructure.provider

import com.yoloo.server.common.api.exception.UnauthorizedException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class DemoProvider : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        println(authentication)

        throw UnauthorizedException("hello")
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }
}