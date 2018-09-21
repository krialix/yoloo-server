package com.yoloo.server.group.api

import com.yoloo.server.auth.AuthUtil
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.group.usecase.GetGroupUseCase
import com.yoloo.server.group.usecase.ListSubscriptionsUseCase
import com.yoloo.server.group.usecase.SubscribeUseCase
import com.yoloo.server.group.usecase.UnsubscribeUseCase
import com.yoloo.server.group.vo.GroupResponse
import com.yoloo.server.group.vo.SubscriptionUserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api/v1/groups",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class GroupController(
    private val getGroupUseCase: GetGroupUseCase,
    private val subscribeUseCase: SubscribeUseCase,
    private val unsubscribeUseCase: UnsubscribeUseCase,
    private val listSubscriptionsUseCase: ListSubscriptionsUseCase
) {

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/{groupId}")
    fun getGroup(authentication: Authentication, @PathVariable("groupId") groupId: Long): GroupResponse {
        val user = AuthUtil.from(authentication)

        return getGroupUseCase.execute(user.userId, groupId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{groupId}/subscribe")
    fun subscribeGroup(authentication: Authentication, @PathVariable("groupId") groupId: Long) {
        val user = AuthUtil.from(authentication)

        subscribeUseCase.execute(user.userId, user.username, user.picture, groupId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{groupId}/subscribe")
    fun unsubscribeGroup(authentication: Authentication, @PathVariable("groupId") groupId: Long) {
        val user = AuthUtil.from(authentication)

        unsubscribeUseCase.execute(user.userId, groupId)
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/{groupId}/subscriptions")
    fun listSubscriptions(
        authentication: Authentication,
        @PathVariable("groupId") groupId: Long,
        @RequestParam(value = "cursor", required = false) cursor: String?
    ): CollectionResponse<SubscriptionUserResponse> {

        return listSubscriptionsUseCase.execute(groupId, cursor)
    }
}