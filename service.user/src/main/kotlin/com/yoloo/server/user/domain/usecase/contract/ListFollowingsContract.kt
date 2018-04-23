package com.yoloo.server.user.domain.usecase.contract

import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.relationship.domain.response.RelationshipResponse

interface ListFollowingsContract {

    data class Request(val userId: String, val cursor: String?)

    data class Response(val response: CollectionResponse<RelationshipResponse>)
}