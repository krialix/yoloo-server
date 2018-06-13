package com.yoloo.server.user.provider

import com.google.firebase.auth.ImportUserRecord
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.UserRegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder

class EmailUserRecordProvider(
    private val idGenerator: LongIdGenerator,
    private val passwordEncoder: PasswordEncoder
) : FirebaseUserRecordProvider {

    override fun provide(request: UserRegisterRequest): ImportUserRecord {
        val hashedPassword = passwordEncoder.encode(request.password)

        return ImportUserRecord.builder()
            .setUid(idGenerator.toString())
            .setDisplayName(request.displayName)
            .setEmail(request.email)
            .setPhotoUrl(request.photoUrl)
            .setEmailVerified(false)
            .setPasswordHash(hashedPassword.toByteArray())
            .putCustomClaim("roles", listOf(User.Role.MEMBER))
            .build()
    }
}