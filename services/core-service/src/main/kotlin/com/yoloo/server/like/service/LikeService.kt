package com.yoloo.server.like.service

import com.yoloo.server.entity.Likeable

interface LikeService {

    fun like(userId: Long, likeableId: Long, type: Class<out Likeable>)

    fun dislike(userId: Long, likeableId: Long, type: Class<out Likeable>)
}
