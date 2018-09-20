package com.yoloo.server.vote.vo

interface Votable {
    fun vote()

    fun unvote()

    fun isVotingAllowed(): Boolean {
        return true
    }
}
