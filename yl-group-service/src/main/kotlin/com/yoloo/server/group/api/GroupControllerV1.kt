package com.yoloo.server.group.api

import com.yoloo.server.group.usecase.GetGroupUseCase
import com.yoloo.server.group.usecase.SubscribeUseCase
import com.yoloo.server.group.usecase.UnsubscribeUseCase
import com.yoloo.server.group.vo.GroupResponse
import com.yoloo.server.group.vo.JwtClaims
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping(
    "/api/v1/groups",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class GroupControllerV1(
    private val getGroupUseCase: GetGroupUseCase,
    private val subscribeUseCase: SubscribeUseCase,
    private val unsubscribeUseCase: UnsubscribeUseCase
) {

    @GetMapping("/{groupId}")
    fun getGroup(authentication: Authentication, @PathVariable("groupId") groupId: Long): GroupResponse {
        val jwtClaim = JwtClaims.from(authentication)

        return getGroupUseCase.execute(jwtClaim.sub, groupId)
    }

    @PostMapping("/{groupId}/subscribe")
    fun subscribeGroup(authentication: Authentication, @PathVariable("groupId") groupId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        subscribeUseCase.execute(jwtClaim.sub, jwtClaim.displayName, jwtClaim.picture, groupId)
    }

    @DeleteMapping("/{groupId}/subscribe")
    fun unsubscribeGroup(authentication: Authentication, @PathVariable("groupId") groupId: Long) {
        val jwtClaim = JwtClaims.from(authentication)

        unsubscribeUseCase.execute(jwtClaim.sub, groupId)
    }
}