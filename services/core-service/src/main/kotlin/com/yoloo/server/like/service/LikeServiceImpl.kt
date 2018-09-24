package com.yoloo.server.like.service

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions.*
import com.yoloo.server.counter.CounterService
import com.yoloo.server.entity.EntityHelper
import com.yoloo.server.entity.Likeable
import com.yoloo.server.like.entity.Like
import com.yoloo.server.like.exception.LikeErrors
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.user.exception.UserErrors
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl(
    private val memcacheService: AsyncMemcacheService,
    private val counterService: CounterService
) : LikeService {

    override fun like(userId: Long, likeableId: Long, type: Class<out Likeable>) {
        val entityFilter = getEntityFilter()

        checkNotFound(entityFilter.contains(userId), UserErrors.ERROR_USER_NOT_FOUND)
        checkNotFound(entityFilter.contains(likeableId), PostErrors.ERROR_POST_NOT_FOUND)

        val like = Like.create(userId, likeableId)

        checkConflict(!entityFilter.contains(like.id), LikeErrors.CONFLICT)

        val votable = ofy().load().key(Key.create(type, likeableId)).now()

        checkForbidden(votable.isVotingAllowed(), LikeErrors.FORBIDDEN)

        entityFilter.insert(like.id)

        updateEntityFilter(entityFilter)

        counterService.increment("likes:$likeableId")
    }

    override fun dislike(userId: Long, likeableId: Long, type: Class<out Likeable>) {
        val entityFilter = getEntityFilter()

        checkNotFound(entityFilter.contains(userId), UserErrors.ERROR_USER_NOT_FOUND)
        checkNotFound(entityFilter.contains(likeableId), PostErrors.ERROR_POST_NOT_FOUND)

        val likeKey = Like.createKey(userId, likeableId)

        checkNotFound(entityFilter.contains(likeKey.name), LikeErrors.CONFLICT)

        updateEntityFilter(entityFilter)
        ofy().delete().key(likeKey)

        counterService.decrement("likes:$likeableId")
    }

    private fun updateEntityFilter(entityFilter: NanoCuckooFilter) {
        memcacheService.put(EntityHelper.KEY_FILTER_ENTITY, entityFilter)
    }

    private fun getEntityFilter() = memcacheService.get(EntityHelper.KEY_FILTER_ENTITY).get() as NanoCuckooFilter
}
