package com.yoloo.server.user.domain.response

data class UserCountResponse(val posts: Int, val followings: Int, val followers: Int)