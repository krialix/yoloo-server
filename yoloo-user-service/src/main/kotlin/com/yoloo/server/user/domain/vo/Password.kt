package com.yoloo.server.user.domain.vo

data class Password(var value: String) {

    init {
        value = value.trim()
    }
}