package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.queue.service.SearchQueueService
import com.yoloo.server.common.queue.vo.EventType
import com.yoloo.server.common.queue.vo.YolooEvent
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.Email
import com.yoloo.server.user.vo.PatchUserRequest
import net.cinnom.nanocuckoo.NanoCuckooFilter

// TODO create a task that updates displayName all other places
class UpdateUserUseCase(
    private val memcacheService: MemcacheService,
    private val searchQueueService: SearchQueueService
) {

    fun execute(requesterId: Long, request: PatchUserRequest) {
        val user = ofy().load().type(User::class.java).id(requesterId).now()

        ServiceExceptions.checkNotFound(user != null, "user.not_found")

        request.displayName?.let {
            user.profile.displayName.value = it
        }

        var userIdentifierFilter: NanoCuckooFilter? = null
        request.email?.let {
            userIdentifierFilter = getUserIdentifierFilter()
            ServiceExceptions.checkBadRequest(!userIdentifierFilter!!.contains(it), "user.email.conflict")

            user.email = Email(it)

            userIdentifierFilter!!.insert(it)
        }

        ofy().transact {
            ofy().save().entity(user)

            addToSearchQueue(user)

            if (userIdentifierFilter != null) {
                memcacheService.put(User.KEY_FILTER_USER_IDENTIFIER, userIdentifierFilter)
            }
        }
    }

    private fun getUserIdentifierFilter(): NanoCuckooFilter {
        return memcacheService.get(User.KEY_FILTER_USER_IDENTIFIER) as NanoCuckooFilter
    }

    private fun addToSearchQueue(user: User) {
        val event = YolooEvent.newBuilder(YolooEvent.Metadata.of(EventType.UPDATE_USER))
            .addData("id", user.id.toString())
            .addData("displayName", user.profile.displayName.value)
            .build()

        searchQueueService.addQueueAsync(event)
    }
}