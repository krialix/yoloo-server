package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Email(var email: String) : Comparable<Email> {

    override fun compareTo(other: Email): Int {
        return email.compareTo(other.email)
    }
}
