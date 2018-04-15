package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.user.domain.response.RelationshipResponse
import org.dialectic.jsonapi.response.DataResponse

interface ListFollowingsUseCaseContract {

    data class Request(val userId: String, val cursor: String?)

    data class Response(val response: DataResponse<RelationshipResponse>)
}