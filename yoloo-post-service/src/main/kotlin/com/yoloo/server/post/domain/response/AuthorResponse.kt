package com.yoloo.server.post.domain.response

import com.yoloo.server.common.response.attachment.AttachmentResponse
import com.yoloo.server.common.util.NoArg

@NoArg
data class AuthorResponse(
    val id: String,
    var self: Boolean,
    val displayName: String,
    val url: String?,
    val image: AttachmentResponse
)