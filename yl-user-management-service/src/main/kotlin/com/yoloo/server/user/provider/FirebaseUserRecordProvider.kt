package com.yoloo.server.user.provider

import com.google.firebase.auth.ImportUserRecord
import com.yoloo.server.user.vo.UserRegisterRequest

interface FirebaseUserRecordProvider {

    fun provide(request: UserRegisterRequest): ImportUserRecord
}