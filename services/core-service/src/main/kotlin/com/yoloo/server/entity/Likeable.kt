package com.yoloo.server.entity

interface Likeable {
    fun vote()

    fun unvote()

    fun isVotingAllowed(): Boolean
}
