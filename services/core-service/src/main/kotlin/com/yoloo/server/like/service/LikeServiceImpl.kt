package com.yoloo.server.like.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.Exceptions.checkException
import com.yoloo.server.counter.CounterService
import com.yoloo.server.entity.Likeable
import com.yoloo.server.filter.Filter
import com.yoloo.server.filter.FilterService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.like.exception.LikeErrors
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.queue.QueueNames
import com.yoloo.server.queue.QueuePayload
import com.yoloo.server.user.exception.UserErrors
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.zalando.problem.Status

@Service
class LikeServiceImpl(
    private val filterService: FilterService,
    private val counterService: CounterService,
    @Qualifier(QueueNames.BATCH_SAVE_PULL_QUEUE) private val batchSavePullQueue: Queue,
    private val objectMapper: ObjectMapper
) : LikeService {

    override fun like(userId: Long, likeableId: Long, type: Class<out Likeable>) {
        val entityCache = filterService.getAll()

        checkException(entityCache.contains(userId), Status.NOT_FOUND, UserErrors.NOT_FOUND)
        checkException(entityCache.contains(likeableId), Status.NOT_FOUND, PostErrors.NOT_FOUND)

        val like = Like.create(userId, likeableId)

        checkException(!entityCache.contains(like.id), Status.CONFLICT, LikeErrors.CONFLICT)

        counterService.increment("LIKE:$likeableId")

        entityCache.add(like.id)
        filterService.saveAsync(entityCache)

        ofy().load().type(Filter::class.java).filter().star

        addToQueue(QueuePayload.save(like))
    }

    override fun dislike(userId: Long, likeableId: Long, type: Class<out Likeable>) {
        val entityCache = filterService.getAll()

        checkException(entityCache.contains(userId), Status.NOT_FOUND, UserErrors.NOT_FOUND)
        checkException(entityCache.contains(likeableId), Status.NOT_FOUND, PostErrors.NOT_FOUND)

        val likeKey = Like.createKey(userId, likeableId)

        checkException(entityCache.contains(likeKey.name), Status.CONFLICT, LikeErrors.CONFLICT)

        counterService.decrement("LIKE:$likeableId")

        entityCache.delete(likeKey.name)
        filterService.saveAsync(entityCache)

        addToQueue(QueuePayload.delete(likeKey.toUrlSafe()))
    }

    private fun addToQueue(payload: QueuePayload) {
        val json = objectMapper.writeValueAsString(payload)
        batchSavePullQueue.addAsync(TaskOptions.Builder.withTag("default").payload(json))
    }
}
