package com.yoloo.server.entity

interface Approvable {
    fun approve()

    fun disapprove()

    fun isApprovingAllowed(): Boolean
}
