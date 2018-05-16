package com.yoloo.server.post.vo.postdataresponse

import com.yoloo.server.post.vo.PostAttachmentResponse

data class SponsoredPostDataResponse(
    val title: String,
    val attachments: List<PostAttachmentResponse>
) : PostDataResponse