package com.yoloo.server.relationship.domain.usecase.contract

import java.security.Principal

interface InsertRelationshipUseCaseContract {

    data class Request(val principal: Principal, val userId: String)
}