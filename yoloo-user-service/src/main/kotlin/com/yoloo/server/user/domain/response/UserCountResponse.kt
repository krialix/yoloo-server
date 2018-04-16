package com.yoloo.server.user.domain.response

data class UserCountResponse(val posts: Int, val followings: Long, val followers: Long)