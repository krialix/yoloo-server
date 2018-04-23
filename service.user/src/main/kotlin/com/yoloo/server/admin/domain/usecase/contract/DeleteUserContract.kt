package com.yoloo.server.admin.domain.usecase.contract

import java.security.Principal

interface DeleteUserContract {

    data class Request(val principal: Principal, val userId: String)
}