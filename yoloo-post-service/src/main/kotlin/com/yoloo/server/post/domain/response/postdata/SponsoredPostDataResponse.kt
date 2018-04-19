package com.yoloo.server.post.domain.response.postdata

import com.yoloo.server.post.domain.response.PostAttachmentResponse

data class SponsoredPostDataResponse(
    val title: String,
    val attachments: List<PostAttachmentResponse>
) : PostDataResponse