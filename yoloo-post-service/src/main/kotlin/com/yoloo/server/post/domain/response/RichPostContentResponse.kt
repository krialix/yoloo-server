package com.yoloo.server.post.domain.response

data class RichPostContentResponse(
    val attachments: List<PostAttachmentResponse>
) : PostContentResponse