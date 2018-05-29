package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.error.exception.ServiceExceptions
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.mapper.UserResponseMapper
import com.yoloo.server.user.vo.UserResponse
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class GetUserUseCase(
    private val memcacheService: MemcacheService,
    private val userResponseMapper: UserResponseMapper
) {

    fun execute(requesterId: Long, targetId: Long): UserResponse {
        val user = ofy().load().type(User::class.java).id(targetId).now()

        ServiceExceptions.checkNotFound(user != null, "user.not_found")

        val self = targetId == requesterId
        val following = when {
            self -> false
            else -> Relationship.isFollowing(getRelationshipFilter(), requesterId, targetId)
        }

        return userResponseMapper.apply(user, self, following)
    }

    private fun getRelationshipFilter(): NanoCuckooFilter {
        return memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter
    }
}