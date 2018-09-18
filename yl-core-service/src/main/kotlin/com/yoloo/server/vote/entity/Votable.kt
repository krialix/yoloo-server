package com.yoloo.server.vote.entity

interface Votable {
    fun vote()

    fun unvote()

    fun isVotingAllowed(): Boolean {
        return true
    }
}
