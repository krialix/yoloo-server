package com.yoloo.server.vote.service

import com.yoloo.server.entity.Votable

interface VoteService {

    fun vote(userId: Long, votableId: Long, type: Class<out Votable>)

    fun unvote(userId: Long, votableId: Long, type: Class<out Votable>)
}
