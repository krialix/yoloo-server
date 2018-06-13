package com.yoloo.server.user.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.yoloo.server.common.id.LongIdGenerator
import com.yoloo.server.user.fetcher.GroupInfoFetcher
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.usecase.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun emailValidationUseCase(memcacheService: MemcacheService): CheckEmailAvailabilityUseCase {
        return CheckEmailAvailabilityUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun getUserUseCase(memcacheService: MemcacheService, userResponseMapper: UserResponseMapper): GetUserUseCase {
        return GetUserUseCase(memcacheService, userResponseMapper)
    }

    @Lazy
    @Bean
    fun patchUserUseCase(): PatchUserUseCase {
        return PatchUserUseCase()
    }

    @Lazy
    @Bean
    fun postCreatedEventUseCase(
        objectMapper: ObjectMapper,
        firebaseMessaging: FirebaseMessaging,
        @Qualifier(EventConfig.EVENT_FILTER) eventFilter: NanoCuckooFilter
    ): PostCreatedEventUseCase {
        return PostCreatedEventUseCase(objectMapper, firebaseMessaging, eventFilter)
    }

    @Lazy
    @Bean
    fun postDeletedEventUseCase(
        objectMapper: ObjectMapper,
        @Qualifier(EventConfig.EVENT_FILTER) eventFilter: NanoCuckooFilter
    ): PostDeletedEventUseCase {
        return PostDeletedEventUseCase(objectMapper, eventFilter)
    }

    @Lazy
    @Bean
    fun commentCreatedEventUseCase(
        objectMapper: ObjectMapper,
        firebaseMessaging: FirebaseMessaging,
        @Qualifier(EventConfig.EVENT_FILTER) eventFilter: NanoCuckooFilter
    ): CommentCreatedEventUseCase {
        return CommentCreatedEventUseCase(objectMapper, firebaseMessaging, eventFilter)
    }

    @Lazy
    @Bean
    fun commentApprovedEventUseCase(
        objectMapper: ObjectMapper,
        firebaseMessaging: FirebaseMessaging,
        @Qualifier(EventConfig.EVENT_FILTER) eventFilter: NanoCuckooFilter
    ): CommentApprovedEventUseCase {
        return CommentApprovedEventUseCase(objectMapper, firebaseMessaging, eventFilter)
    }

    @Lazy
    @Bean
    fun commentDeletedEventUseCase(
        objectMapper: ObjectMapper,
        @Qualifier(EventConfig.EVENT_FILTER) eventFilter: NanoCuckooFilter
    ): CommentDeletedEventUseCase {
        return CommentDeletedEventUseCase(objectMapper, eventFilter)
    }

    @Lazy
    @Bean
    fun searchUserUseCase(): SearchUserUseCase {
        return SearchUserUseCase()
    }

    @Lazy
    @Bean
    fun registerUserUseCase(
        groupInfoFetcher: GroupInfoFetcher,
        @Qualifier("cached") idGenerator: LongIdGenerator,
        eventPublisher: ApplicationEventPublisher,
        objectMapper: ObjectMapper,
        memcacheService: MemcacheService,
        firebaseAuth: FirebaseAuth,
        passwordEncoder: PasswordEncoder
    ): UserRegisterUseCase {
        return UserRegisterUseCase(
            groupInfoFetcher,
            idGenerator,
            objectMapper,
            firebaseAuth,
            memcacheService,
            passwordEncoder
        )
    }
}