package com.yoloo.server.user.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.util.id.LongIdGenerator
import com.yoloo.server.user.fetcher.GroupInfoFetcher
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.usecase.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.oauth2.client.token.AccessTokenProvider
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails

@Configuration
class UseCaseConfig {

    @Lazy
    @Bean
    fun emailValidationUseCase(memcacheService: MemcacheService): EmailValidationUseCase {
        return EmailValidationUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun followUseCase(memcacheService: MemcacheService): FollowUseCase {
        return FollowUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun unfollowUseCase(memcacheService: MemcacheService): UnfollowUseCase {
        return UnfollowUseCase(memcacheService)
    }

    @Lazy
    @Bean
    fun getUserUseCase(memcacheService: MemcacheService, userResponseMapper: UserResponseMapper): GetUserUseCase {
        return GetUserUseCase(memcacheService, userResponseMapper)
    }

    @Lazy
    @Bean
    fun listRelationshipsUseCase(): ListRelationshipUseCase {
        return ListRelationshipUseCase()
    }

    @Lazy
    @Bean
    fun patchUserUseCase(): PatchUserUseCase {
        return PatchUserUseCase()
    }

    @Lazy
    @Bean
    fun postCreatedEventUseCase(objectMapper: ObjectMapper): PostCreatedEventUseCase {
        return PostCreatedEventUseCase(objectMapper)
    }

    @Lazy
    @Bean
    fun searchUserUseCase(): SearchUserUseCase {
        return SearchUserUseCase()
    }

    @Lazy
    @Bean
    fun signUpEmailUseCase(
        groupInfoFetcher: GroupInfoFetcher,
        @Qualifier("cached") idGenerator: LongIdGenerator,
        eventPublisher: ApplicationEventPublisher,
        objectMapper: ObjectMapper,
        memcacheService: MemcacheService,
        resourceOwnerPasswordResourceDetails: ResourceOwnerPasswordResourceDetails,
        accessTokenProvider: AccessTokenProvider
    ): SignUpEmailUseCase {
        return SignUpEmailUseCase(
            groupInfoFetcher,
            idGenerator,
            eventPublisher,
            objectMapper,
            memcacheService,
            resourceOwnerPasswordResourceDetails,
            accessTokenProvider
        )
    }
}