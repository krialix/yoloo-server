package com.yoloo.server.user.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.http.auth.BasicUserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class GetUserUseCase @Autowired constructor(
    private val userResponseMapper: UserResponseMapper,
    private val memcacheService: MemcacheService
) : UseCase<GetUserUseCase.Request, UserResponse> {

    override fun execute(request: Request): UserResponse {
        val targetId = request.userId

        var user = ofy().load().type(User::class.java).id(targetId).now()

        User.checkUserExistsAndEnabled(user)

        val requesterId = request.principal?.name?.toLong() ?: BasicUserPrincipal("101010").name.toLong()

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

    class Request(val principal: Principal?, val userId: Long)
}