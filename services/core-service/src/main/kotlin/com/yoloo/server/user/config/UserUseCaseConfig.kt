package com.yoloo.server.user.config

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.memcache.MemcacheService
import com.google.firebase.auth.FirebaseAuth
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.usecase.*
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory.LongIdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserUseCaseConfig {

    @Lazy
    @Bean
    fun emailValidationUseCase(memcacheService: MemcacheService): CheckEmailAvailabilityUseCase {
        return CheckEmailAvailabilityUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun registerUserUseCase(
        idGenerator: LongIdGenerator,
        memcacheService: AsyncMemcacheService,
        firebaseAuth: FirebaseAuth,
        passwordEncoder: PasswordEncoder,
        userResponseMapper: UserResponseMapper
    ): CreateUserUseCase {
        return CreateUserUseCase(
            idGenerator,
            firebaseAuth,
            memcacheService,
            passwordEncoder,
            userResponseMapper
        )
    }

    @Lazy
    @Bean
    fun getUserUseCase(memcacheService: MemcacheService, userResponseMapper: UserResponseMapper): GetUserUseCase {
        return GetUserUseCase(memcacheService, userResponseMapper)
    }

    @Lazy
    @Bean
    fun updateUserUseCase(memcacheService: MemcacheService): UpdateUserUseCase {
        return UpdateUserUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun searchUserUseCase(): SearchUserUseCase {
        return SearchUserUseCase()
    }
}
