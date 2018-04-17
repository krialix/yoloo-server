package com.yoloo.server.relationship.domain.response

import com.yoloo.server.common.util.NoArg

@NoArg
data class RelationshipResponse(val id: String, val displayName: String, val avatarUrl: String)