package com.yoloo.server.post.response.postdata

import com.yoloo.server.post.response.PostAttachmentResponse

data class SponsoredPostDataResponse(
    val title: String,
    val attachments: List<PostAttachmentResponse>
) : PostDataResponse