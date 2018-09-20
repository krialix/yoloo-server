package com.yoloo.server.relationship.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkConflict
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class FollowUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(fromId: Long, toId: Long) {
        val map = ofy().load().type(User::class.java).ids(fromId, toId)
        val fromUser = map[fromId]
        val toUser = map[toId]

        ServiceExceptions.checkNotFound(toUser != null, "user.not_found")

        val relationshipFilter = memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP).get() as NanoCuckooFilter

        checkConflict(!isFollowing(relationshipFilter, fromId, toId), "relationship.conflict")

        fromUser!!.follow(toUser!!)

        val relationship = Relationship.create(fromUser, toUser)
        relationshipFilter.insert(relationship.id)

        ofy().save().entities(fromUser, toUser, relationship)

        memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, relationshipFilter)

        addToNotificationQueue(toUser.fcmToken, fromUser)
    }

    private fun addToNotificationQueue(toUserFcmToken: String, fromUser: User) {
        /*val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.FOLLOW_USER))
                .addData("fromUserId", fromUser.id.toString())
                .addData("fromUserDisplayName", fromUser.profile.displayName.value)
                .addData("toUserFcmToken", toUserFcmToken)
                .build()

        notificationQueueService.addQueueAsync(event)*/
    }

    private fun isFollowing(filter: NanoCuckooFilter, fromId: Long, toId: Long): Boolean {
        return filter.contains(Relationship.createId(fromId, toId))
    }
}
