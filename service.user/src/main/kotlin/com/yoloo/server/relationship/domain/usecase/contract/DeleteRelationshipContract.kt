package com.yoloo.server.relationship.domain.usecase.contract

import java.security.Principal

interface DeleteRelationshipContract {

    data class Request(val principal: Principal, val userId: String)
}