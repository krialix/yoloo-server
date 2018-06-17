package com.yoloo.server.user.provider

import com.google.firebase.auth.ImportUserRecord
import com.google.firebase.auth.UserImportHash
import com.google.firebase.auth.UserImportOptions
import com.yoloo.server.user.vo.UserRegisterRequest

interface FirebaseUserRecordProvider {

    fun provide(request: UserRegisterRequest): Pair<ImportUserRecord, UserImportOptions?>
}