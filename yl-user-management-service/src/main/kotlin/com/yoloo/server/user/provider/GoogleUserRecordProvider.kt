package com.yoloo.server.user.provider

import com.google.firebase.auth.ImportUserRecord
import com.google.firebase.auth.UserProvider
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.UserRegisterRequest

class GoogleUserRecordProvider(private val idGenerator: LongIdGenerator) : FirebaseUserRecordProvider {

    override fun provide(request: UserRegisterRequest): ImportUserRecord {
        return ImportUserRecord.builder()
            .setUid(idGenerator.toString())
            .setDisplayName(request.displayName)
            .setEmail(request.email)
            .setPhotoUrl(request.photoUrl)
            .setEmailVerified(false)
            .putCustomClaim("roles", listOf(User.Role.MEMBER))
            .addUserProvider(
                UserProvider.builder()
                    .setUid(request.providerUid)
                    .setEmail(request.email)
                    .setDisplayName(request.displayName)
                    .setPhotoUrl(request.photoUrl)
                    .setProviderId("google.com")
                    .build()
            )
            .build()
    }
}