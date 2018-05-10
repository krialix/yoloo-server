package com.yoloo.server.user.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.domain.vo.JwtClaims
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class GetUserUseCase(
    private val userResponseMapper: UserResponseMapper,
    private val memcacheService: MemcacheService
) : UseCase<GetUserUseCase.Params, UserResponse> {

    override fun execute(params: Params): UserResponse {
        val requesterId = params.jwtClaims.sub
        val targetId = params.targetId

        var user = ofy().load().type(User::class.java).id(targetId).now()

        val self = targetId == requesterId

        user = if (self) {
            user.copy(self = true, following = false)
        } else {
            val relationshipFilter = memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter

            user.copy(
                self = requesterId == targetId,
                following = relationshipFilter.contains("$requesterId:$targetId")
            )
        }

        return userResponseMapper.apply(user)
    }

    class Params(val jwtClaims: JwtClaims, val targetId: Long)
}