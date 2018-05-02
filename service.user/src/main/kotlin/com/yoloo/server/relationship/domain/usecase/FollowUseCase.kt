package com.yoloo.server.relationship.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.common.util.TimestampIdGenerator
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.user.domain.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class FollowUseCase(private val memcacheService: MemcacheService) : UseCase<FollowUseCase.Request, Unit> {

    override fun execute(request: Request) {
        val id = TimestampIdGenerator.generateId()
        val fromId = request.principal.name
        val toId = request.userId

        val toUser = ofy().load().type(User::class.java).id(toId).now()

        if (toUser == null || !toUser.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        val cacheIds = listOf(
            "counter_follower:$toId",
            "counter_following:$fromId",
            "filter_follower:$toId",
            "filter_following:$fromId"
        )

        val values = memcacheService.getAll(cacheIds)

        val followerCount = values["counter_follower:$toId"] as Long? ?: 0L
        val followingCount = values["counter_following:$fromId"] as Long? ?: 0L
        val followerCuckooFilter =
            values["filter_follower:$toId"] as NanoCuckooFilter? ?: NanoCuckooFilter.Builder(32).build()
        val followingCuckooFilter =
            values["filter_following:$fromId"] as NanoCuckooFilter? ?: NanoCuckooFilter.Builder(32).build()

        val updatedCache = mapOf(
            "counter_follower:$toId" to followerCount.inc(),
            "counter_following:$fromId" to followingCount.inc(),
            "filter_follower:$toId" to followerCuckooFilter.insert(fromId),
            "filter_following:$fromId" to followingCuckooFilter.insert(toId)
        )

        memcacheService.putAll(updatedCache)

        val relationship = Relationship(
            id = id,
            fromId = fromId,
            toId = toId,
            displayName = toUser.displayName,
            avatarImage = toUser.image
        )

        ofy().save().entity(relationship)
    }

    class Request(val principal: Principal, val userId: String)
}