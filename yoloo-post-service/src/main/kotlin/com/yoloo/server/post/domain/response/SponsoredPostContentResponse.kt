package com.yoloo.server.post.domain.response

data class SponsoredPostContentResponse(
    val title: String,
    val content: String,
    val attachments: List<PostAttachmentResponse>
) : PostContentResponse