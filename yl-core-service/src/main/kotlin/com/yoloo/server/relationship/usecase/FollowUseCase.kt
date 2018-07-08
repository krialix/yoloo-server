package com.yoloo.server.relationship.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkConflict
import com.yoloo.server.common.queue.service.NotificationQueueService
import com.yoloo.server.common.queue.vo.EventType
import com.yoloo.server.common.queue.vo.YolooEvent
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter

class FollowUseCase(
    private val memcacheService: AsyncMemcacheService,
    private val notificationQueueService: NotificationQueueService
) {

    fun execute(requesterId: Long, userId: Long) {
        val map = ofy().load().type(User::class.java).ids(requesterId, userId)
        val fromUser = map[requesterId]
        val toUser = map[userId]

        ServiceExceptions.checkNotFound(toUser != null, "user.not_found")

        val relationshipFilter = getRelationshipFilter()

        checkConflict(
            !Relationship.isFollowing(relationshipFilter, requesterId, userId),
            "relationship.conflict"
        )

        fromUser!!.profile.countData.followingCount = fromUser.profile.countData.followingCount.inc()
        toUser!!.profile.countData.followerCount = fromUser.profile.countData.followerCount.inc()

        val relationship =
            Relationship.create(fromUser.id, fromUser.profile.displayName, fromUser.profile.profileImageUrl, toUser.id)
        relationshipFilter.insert(relationship.id)

        val saveResult = ofy().save().entities(fromUser, toUser, relationship)
        val putFuture = memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, relationshipFilter)

        TestUtil.saveNow(saveResult)
        TestUtil.saveNow(putFuture)

        addToNotificationQueue(toUser.fcmToken, fromUser)
    }

    private fun getRelationshipFilter(): NanoCuckooFilter {
        return memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP).get() as NanoCuckooFilter
    }

    private fun addToNotificationQueue(toUserFcmToken: String, fromUser: User) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.FOLLOW_USER))
            .addData("fromUserId", fromUser.id.toString())
            .addData("fromUserDisplayName", fromUser.profile.displayName.value)
            .addData("toUserFcmToken", toUserFcmToken)
            .build()

        notificationQueueService.addQueueAsync(event)
    }
}