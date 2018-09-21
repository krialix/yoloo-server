package com.yoloo.server.entity

interface Votable {
    fun vote()

    fun unvote()

    fun isVotingAllowed(): Boolean
}
