package com.yoloo.server.user.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.entity.service.EntityCacheService
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.exception.UserErrors
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.vo.UserResponse

class GetUserUseCase(
    private val hashids: Hashids,
    private val entityCacheService: EntityCacheService,
    private val userResponseMapper: UserResponseMapper
) : AbstractUseCase<GetUserUseCase.Input, UserResponse>() {

    override fun onExecute(input: Input): UserResponse {
        val requesterId = hashids.decode(input.requesterId)[0]
        val targetId = hashids.decode(input.targetId)[0]

        val entityCache = entityCacheService.get()

        checkNotFound(entityCache.contains(requesterId), UserErrors.NOT_FOUND)
        checkNotFound(entityCache.contains(targetId), UserErrors.NOT_FOUND)

        val user = ofy().load().key(User.createKey(targetId)).now()

        val self = targetId == requesterId
        val following = entityCache.contains(Relationship.createId(requesterId, targetId))

        return userResponseMapper.apply(user, self, following)
    }

    data class Input(val requesterId: String, val targetId: String) : UseCase.Input
}
