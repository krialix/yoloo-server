package com.yoloo.server.user.config

import com.google.appengine.api.memcache.MemcacheService
import com.google.firebase.auth.FirebaseAuth
import com.yoloo.server.common.id.config.IdBeanQualifier
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.common.queue.service.NotificationQueueService
import com.yoloo.server.common.queue.service.SearchQueueService
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.usecase.*
import org.springframework.beans.factory.annotation.Qualifier
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
        @Qualifier(IdBeanQualifier.CACHED) idGenerator: LongIdGenerator,
        memcacheService: MemcacheService,
        firebaseAuth: FirebaseAuth,
        passwordEncoder: PasswordEncoder,
        userResponseMapper: UserResponseMapper,
        notificationQueueService: NotificationQueueService,
        searchQueueService: SearchQueueService
    ): CreateUserUseCase {
        return CreateUserUseCase(
            idGenerator,
            firebaseAuth,
            memcacheService,
            passwordEncoder,
            userResponseMapper,
            notificationQueueService,
            searchQueueService
        )
    }

    @Lazy
    @Bean
    fun getUserUseCase(memcacheService: MemcacheService, userResponseMapper: UserResponseMapper): GetUserUseCase {
        return GetUserUseCase(memcacheService, userResponseMapper)
    }

    @Lazy
    @Bean
    fun updateUserUseCase(memcacheService: MemcacheService, searchQueueService: SearchQueueService): UpdateUserUseCase {
        return UpdateUserUseCase(memcacheService, searchQueueService)
    }

    @Lazy
    @Bean
    fun searchUserUseCase(): SearchUserUseCase {
        return SearchUserUseCase()
    }
}