package com.yoloo.server.user.provider

import com.google.firebase.auth.ImportUserRecord
import com.google.firebase.auth.UserImportOptions
import com.yoloo.server.user.vo.UserCreateRequest

interface FirebaseUserRecordProvider {

    fun provide(request: UserCreateRequest): Pair<ImportUserRecord, UserImportOptions?>
}