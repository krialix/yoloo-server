package com.yoloo.server.user.provider

import com.google.firebase.auth.ImportUserRecord
import com.google.firebase.auth.UserImportHash
import com.google.firebase.auth.UserImportOptions
import com.google.firebase.auth.hash.Bcrypt
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.UserRegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder

class EmailUserRecordProvider(
    private val idGenerator: LongIdGenerator,
    private val passwordEncoder: PasswordEncoder
) : FirebaseUserRecordProvider {

    override fun provide(request: UserRegisterRequest): Pair<ImportUserRecord, UserImportOptions?> {
        val hashedPassword = passwordEncoder.encode(request.password)

        val userRecord = ImportUserRecord.builder()
            .setUid(idGenerator.generateId().toString())
            .setDisplayName(request.displayName)
            .setEmail(request.email)
            .setPhotoUrl(request.photoUrl)
            .setEmailVerified(false)
            .setPasswordHash(hashedPassword.toByteArray())
            .putCustomClaim("roles", listOf(User.Role.MEMBER.name))
            .build()

        val importOptions = UserImportOptions.withHash(Bcrypt.getInstance())

        return Pair(userRecord, importOptions)
    }
}