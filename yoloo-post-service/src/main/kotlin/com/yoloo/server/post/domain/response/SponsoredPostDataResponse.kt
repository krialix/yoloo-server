package com.yoloo.server.post.domain.response

data class SponsoredPostDataResponse(
    val title: String,
    val content: String,
    val attachments: List<PostAttachmentResponse>
) : PostDataResponse