package com.yoloo.server.relationship.domain.usecase.impl

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.usecase.DeleteRelationshipUseCase
import com.yoloo.server.relationship.domain.usecase.contract.DeleteRelationshipContract
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class DeleteRelationshipUseCaseImpl(private val memcacheService: MemcacheService) :
    DeleteRelationshipUseCase {

    override fun execute(request: DeleteRelationshipContract.Request) {
        val userId = request.userId
        val requesterId = request.principal.name

        val cacheIds = listOf(
            "counter_follower:$userId",
            "counter_following:$requesterId",
            "filter_follower:$userId",
            "filter_following:$requesterId"
        )

        val values = memcacheService.getAll(cacheIds)

        val followerCount = values["counter_follower:$userId"] as Long? ?: 0L
        val followingCount = values["counter_following:$requesterId"] as Long? ?: 0L
        val followerCuckooFilter =
            values["filter_follower:$userId"] as NanoCuckooFilter? ?: NanoCuckooFilter.Builder(32).build()
        val followingCuckooFilter =
            values["filter_following:$requesterId"] as NanoCuckooFilter? ?: NanoCuckooFilter.Builder(32).build()

        val updatedCache = mapOf(
            "counter_follower:$userId" to followerCount.dec(),
            "counter_following:$requesterId" to followingCount.dec(),
            "filter_follower:$userId" to followerCuckooFilter.delete(requesterId),
            "filter_following:$requesterId" to followingCuckooFilter.delete(userId)
        )

        memcacheService.putAll(updatedCache)

        val relationshipKey = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(Relationship.TO_ID, userId)
            .keys()
            .first()
            .now()

        ofy().delete().key(relationshipKey)
    }
}